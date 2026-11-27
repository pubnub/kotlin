package com.pubnub.docs.support.tools

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.pubnub.docs.support.Tier
import java.io.File

/**
 * Docs cross-check: does every snippet the docs embed from `pubnub/kotlin` map to a test that runs it?
 *
 * The checkout comes from the `DOCS_REPO` env var. It parses every `<EmbeddedCode>` tag in `docs/sdks/kotlin` and
 * `docs/sdks/java` whose url points at pubnub/kotlin and classifies each requested id (or whole file embed) as `live`,
 * `compile-only`, `exception` (snippet-exceptions.json) or `NOT COVERED`.
 *
 * - A url into the tier (the new test files) is covered when the file exists, the extractor finds the id in it and
 *   `coverage.json` lists the id for that file.
 * - A url into the old docs modules is covered when the `snippet-map` of `coverage.json` maps it to a covered new id. The
 *   status then says "repoint pending", because the docs still read the old file until a docs change repoints the tag.
 * - Anything else, a branch other than master and a whole file embed without an exception are NOT COVERED.
 *
 * The docs checkout is only read. No network.
 */
object DocsCheck {
    private val tag = Regex("""<EmbeddedCode\b[\s\S]*?/>""")
    private val url = Regex("""url=\{?['"](https://raw\.githubusercontent\.com/pubnub/kotlin/([^'"]+))['"]\}?""")
    private val snippets = Regex("""snippets=\{\[([^\]]*)\]\}""")
    private val prefixes = Regex("""prefixes=\{\[([^\]]*)\]\}""")
    private val quoted = Regex("""['"]([^'"]*)['"]""")
    private val refAndPath = Regex("""^refs/heads/([^/]+)/(.*)$""")

    class Embed(val docsFile: String, val line: Int, val ref: String, val path: String, val ids: List<String>, val prefixes: List<String>)

    class Entry(val embed: Embed, val id: String?, val status: String, val why: String)

    private val tierPaths = listOf(Tier.KOTLIN_TIER_PATH + "/", Tier.JAVA_TIER_PATH + "/")

    private fun quotedValues(
        regex: Regex,
        tagText: String,
    ): List<String>? {
        val match = regex.find(tagText) ?: return null
        return quoted.findAll(match.groupValues[1]).map { it.groupValues[1] }.toList()
    }

    private fun embedsOf(
        root: File,
        md: File,
    ): List<Embed> {
        val text = md.readText()
        val out = mutableListOf<Embed>()
        for (match in tag.findAll(text)) {
            val tail = url.find(match.value)?.groupValues?.get(2) ?: continue
            val split = refAndPath.matchEntire(tail)
            val ref = split?.groupValues?.get(1) ?: tail.substringBefore('/')
            val path = split?.groupValues?.get(2) ?: tail.substringAfter('/')
            val ids = quotedValues(snippets, match.value)?.filter { it.isNotBlank() } ?: emptyList()
            val prefixList = quotedValues(prefixes, match.value) ?: Extractor.DEFAULT_PREFIXES
            val line = text.substring(0, match.range.first).count { it == '\n' } + 1
            out.add(Embed(md.relativeTo(root).path, line, ref, path, ids, prefixList))
        }
        return out
    }

    @JvmStatic
    fun embeds(root: File): List<Embed> {
        val out = mutableListOf<Embed>()
        for (sub in listOf("kotlin", "java")) {
            val base = File(root, "docs/sdks/$sub")
            if (base.isDirectory) {
                val pages = base.walkTopDown().filter { it.isFile && (it.extension == "md" || it.extension == "mdx") }
                pages.sortedBy { it.path }.forEach { out.addAll(embedsOf(root, it)) }
            }
        }
        return out
    }

    private fun exceptionFor(
        exceptions: List<JsonObject>,
        path: String,
        id: String?,
    ): JsonObject? {
        return exceptions.firstOrNull { e ->
            val ids = e.getAsJsonArray("ids")
            e.get("path")?.asString == path && (ids == null || (id != null && ids.any { it.asString == id }))
        }
    }

    private class Context(
        val manifest: JsonObject,
        val repoRoot: File,
        val allowRef: String?,
    )

    private fun classifyInTier(
        embed: Embed,
        id: String?,
        context: Context,
    ): Pair<String, String> {
        val file = File(context.repoRoot, embed.path)
        if (!file.isFile) {
            return "NOT COVERED" to "${embed.path} does not exist"
        }
        if (id == null) {
            if (context.manifest.getAsJsonObject("whole_files")?.has(embed.path) == true) {
                return "live" to "whole file"
            }
            return "NOT COVERED" to "whole file embed of a file that no test runs"
        }
        if (Extractor.region(file.readText(), id, embed.prefixes) == null) {
            return "NOT COVERED" to "the extractor finds no \"$id\" in ${embed.path}"
        }
        val entry = context.manifest.getAsJsonObject("files")?.getAsJsonObject(embed.path)?.getAsJsonObject(id)
        if (entry == null) {
            return "NOT COVERED" to "coverage.json does not list \"$id\" for ${embed.path}"
        }
        return entry.get("mode").asString to (entry.get("test")?.asString ?: entry.get("reason")?.asString.orEmpty())
    }

    private fun classifyOldPath(
        embed: Embed,
        id: String?,
        context: Context,
    ): Pair<String, String> {
        if (id == null) {
            return "NOT COVERED" to "whole file embed without an exception"
        }
        val target = context.manifest.getAsJsonObject("snippet-map")?.get("${embed.path}#$id")?.asString
        val files = context.manifest.getAsJsonObject("files")
        val entry = target?.let { files?.getAsJsonObject(it.substringBefore('#'))?.getAsJsonObject(it.substringAfter('#')) }
        if (entry == null) {
            return "NOT COVERED" to "not ported to the tier yet"
        }
        return entry.get("mode").asString to "repoint pending: $target"
    }

    @JvmStatic
    @JvmOverloads
    fun classify(
        root: File,
        allowRef: String? = null,
        manifest: JsonObject = Manifest.read(),
        exceptionsFile: File = Manifest.exceptionsFile,
        repoRoot: File = Tier.repoRoot,
    ): List<Entry> {
        val exceptions =
            if (exceptionsFile.isFile) {
                JsonParser.parseString(exceptionsFile.readText()).asJsonObject.getAsJsonArray("exceptions")?.map { it.asJsonObject }
            } else {
                null
            } ?: emptyList()
        val context = Context(manifest, repoRoot, allowRef)
        val results = mutableListOf<Entry>()
        for (embed in embeds(root)) {
            for (id in embed.ids.ifEmpty { listOf<String?>(null) }) {
                val exception = exceptionFor(exceptions, embed.path, id)
                val inTier = tierPaths.any { embed.path.startsWith(it) || embed.path.contains("/$it") }
                val (status, why) =
                    when {
                        exception != null -> "exception" to exception.get("reason").asString
                        embed.ref != "master" && embed.ref != allowRef -> "NOT COVERED" to "ref ${embed.ref} (only master or the allowed ref is covered)"
                        inTier -> classifyInTier(embed, id, context)
                        else -> classifyOldPath(embed, id, context)
                    }
                results.add(Entry(embed, id, status, why))
            }
        }
        return results
    }

    /** The docs checkout from `DOCS_REPO`, or null. */
    @JvmStatic
    fun docsRepo(): File? {
        val given = System.getenv("DOCS_REPO")
        if (given.isNullOrBlank()) {
            return null
        }
        return File(given).takeIf { File(it, "docs/sdks").isDirectory }
    }

    /** Prints the report and returns the number of NOT COVERED entries. */
    @JvmStatic
    @JvmOverloads
    fun report(
        root: File,
        verbose: Boolean = false,
    ): Int {
        val results = classify(root)
        if (verbose) {
            results.filter { it.status == "NOT COVERED" }.forEach {
                println("docs-check: NOT COVERED ${it.embed.docsFile}:${it.embed.line} ${it.id ?: "(whole file)"}: ${it.why}")
            }
        }
        val reasons = results.filter { it.status == "NOT COVERED" }.groupingBy { it.why.replace(Regex("\"[^\"]*\""), "...") }.eachCount()
        reasons.entries.sortedByDescending { it.value }.forEach { println("docs-check: NOT COVERED x${it.value}: ${it.key}") }
        val byStatus = results.groupingBy { it.status }.eachCount().toSortedMap().entries.joinToString(", ") { "${it.key} ${it.value}" }
        val pending = results.count { it.why.startsWith("repoint pending") }
        println("docs-check: ${results.size} embedded ids and whole files from ${root.name}: $byStatus ($pending repoint pending)")
        return results.count { it.status == "NOT COVERED" }
    }
}
