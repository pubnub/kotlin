package com.pubnub.docs.support.tools

import com.google.gson.JsonParser
import com.pubnub.docs.support.Tier
import java.io.File

/**
 * The goldens: what the docs site rendered from the old docs module files at the commit in DocsToolsTest, produced by its
 * TypeScript extractor (`docs-goldens.json`, key `<old repo path>#<id>`). Embedded ids and orphan ids (defined in the old
 * modules, embedded nowhere) are both in it.
 */
object Goldens {
    class Golden(val docset: String, val prefixes: List<String>, val otherPrefixes: List<List<String>>, val text: String)

    @JvmStatic
    fun load(file: File = Tier.file("docs-goldens.json")): Map<String, Golden> {
        if (!file.isFile) {
            return emptyMap()
        }
        val root = JsonParser.parseString(file.readText()).asJsonObject
        return root.entrySet().associate { (key, value) ->
            val entry = value.asJsonObject

            fun list(array: com.google.gson.JsonArray) = array.map { it.asString }
            key to
                Golden(
                    docset = entry.get("docset").asString,
                    prefixes = list(entry.getAsJsonArray("prefixes")),
                    otherPrefixes = entry.getAsJsonArray("otherPrefixes")?.map { list(it.asJsonArray) } ?: emptyList(),
                    text = entry.get("text").asString,
                )
        }
    }
}

/**
 * Equivalence check: every golden is compared with the region now found in the new test files, through the `snippet-map`
 * of `coverage.json`. The text must be identical, whitespace included. A golden that is not mapped yet is reported as "not
 * yet ported" and does not fail, unless `LIVE_EQUIVALENCE_STRICT=1` (used at the end of the port).
 */
object Equivalence {
    class Report(
        val total: Int,
        val identical: List<String>,
        val differ: List<String>,
        val notPorted: List<String>,
    ) {
        fun summary() = "equivalence: $total goldens, ${identical.size} identical, ${differ.size} differ, ${notPorted.size} not yet ported"
    }

    private fun firstDifference(
        expected: String,
        actual: String,
    ): String {
        val a = expected.split("\n")
        val b = actual.split("\n")
        val index = (0 until maxOf(a.size, b.size)).firstOrNull { a.getOrNull(it) != b.getOrNull(it) } ?: return "no line differs"
        return "line ${index + 1} differs (expected ${a.size} lines, found ${b.size})"
    }

    @JvmStatic
    @JvmOverloads
    fun compare(
        goldens: Map<String, Goldens.Golden> = Goldens.load(),
        manifest: com.google.gson.JsonObject = Manifest.read(),
        root: File = Tier.repoRoot,
    ): Report {
        val map = manifest.getAsJsonObject("snippet-map")
        val identical = mutableListOf<String>()
        val differ = mutableListOf<String>()
        val notPorted = mutableListOf<String>()
        for ((key, golden) in goldens.toSortedMap()) {
            val target = map?.get(key)?.asString
            if (target == null) {
                notPorted.add(key)
                continue
            }
            val file = File(root, target.substringBefore('#'))
            val id = target.substringAfter('#')
            if (!file.isFile) {
                differ.add("$key: $target does not exist")
                continue
            }
            val text = file.readText()
            val found = Extractor.region(text, id, golden.prefixes)
            when {
                found == null -> differ.add("$key: the region $target is empty or missing")
                found != golden.text -> differ.add("$key: $target ${firstDifference(golden.text, found)}")
                else -> identical.add(key)
            }
        }
        return Report(goldens.size, identical, differ, notPorted)
    }

    /** Problems for the offline test: every difference, and every unported golden when [strict]. */
    @JvmStatic
    @JvmOverloads
    fun problems(
        report: Report = compare(),
        strict: Boolean = System.getenv("LIVE_EQUIVALENCE_STRICT") == "1",
    ): List<String> {
        if (strict) {
            return report.differ + report.notPorted.map { "$it: not ported" }
        }
        return report.differ
    }
}
