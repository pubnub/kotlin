package com.pubnub.docs.support.tools

import com.pubnub.docs.support.Tier
import java.io.File

/**
 * Shared scanner of the tier: lists the files that can hold markers, scans the markers with the syntax of the docs
 * extractor and maps each region to its enclosing test method. The marker lint, the coverage manifest, the docs
 * cross-check and the equivalence check build on it. It reads files only, nothing is compiled or run.
 *
 * The scan is line based. A method is found by its declaration line and its end by counting braces, which is reliable for
 * code that ktlint and the Java conventions keep formatted. Files in `support/` and `offline/` are not scanned: they hold
 * marker text in fixtures.
 */
object SnippetScan {
    /** A folder to scan: [docset] is `kotlin` or `java`, [extension] includes the dot. */
    data class Root(val dir: File, val docset: String, val extension: String, val base: File)

    data class Problem(val file: String, val line: Int, val rule: String, val message: String) {
        override fun toString() = "$file:$line: $rule $message"
    }

    /** One snippet region. Lines are 1 based. [endLine] is the end marker, or the line after the last one when missing. */
    data class Region(
        val id: String,
        val startLine: Int,
        val endLine: Int,
        val hidden: List<Pair<Int, Int?>>,
        val visible: List<Pair<Int, String>>,
        val spaceless: Boolean,
        val origin: String?,
    )

    /** A method with its annotations. [startLine] is the first annotation line, [endLine] the closing brace. */
    data class Method(
        val name: String,
        val startLine: Int,
        val declLine: Int,
        val endLine: Int,
        val isTest: Boolean,
        val compileOnly: String? = null,
    )

    class ScannedFile(
        val path: File,
        val rel: String,
        val docset: String,
        val kind: String,
        val lines: List<String>,
        val regions: List<Region>,
        val problems: List<Problem>,
        val methods: List<Method>,
    ) {
        val text: String get() = lines.joinToString("\n")

        fun slice(
            from: Int,
            to: Int,
        ): String = lines.subList((from - 1).coerceAtLeast(0), to.coerceAtMost(lines.size)).joinToString("\n")
    }

    /** A region together with who runs it. [owner]: `test`, `helper`, `sample`, `compile-only` or `none`. */
    data class Snippet(
        val id: String,
        val file: ScannedFile,
        val region: Region,
        val owner: String,
        val holder: Method?,
        val test: Method?,
    ) {
        val testId: String? get() = test?.let { "${file.rel.substringAfter("/com/pubnub/docs/")}::${it.name}" }
    }

    private val marker = Regex("""^\s*//(\s*)snippet\.([A-Za-z0-9_-]+)\s*$""")
    private val anyMention = Regex("""^\s*//.*\bsnippet\.""")
    private val blueprint = Regex("""^\s*//\s*blueprint\.[A-Za-z0-9_-]+\s*$""")
    private val originLine = Regex("""^\s*//\s*origin:\s*(\S+#\S+)\s*$""")
    private val kotlinFun =
        Regex(
            """^\s*(?:(?:public|private|internal|protected|override|suspend|inline|open)\s+)*fun\s+""" +
                """(?:<[^>]+>\s+)?(?:[\w.<>?]+\.)?`?([A-Za-z0-9_ ]+?)`?\s*\(""",
        )
    private val javaMethod =
        Regex(
            """^\s*(?:(?:public|private|protected|static|final|synchronized)\s+)*(?:<[^>]+>\s+)?""" +
                """[\w<>\[\],.?]+(?:\s+[\w<>\[\],.?]+)*\s+(\w+)\s*\([^;]*$""",
        )
    private val javaNotMethod = Regex("""^\s*(?:if|for|while|switch|catch|return|new|else|try|throw|do)\b""")
    private val testAnnotation = Regex("""^\s*@(?:Test|ParameterizedTest|RepeatedTest|TestFactory)\b""")
    private val compileOnlyAnnotation = Regex("""@CompileOnly\(\s*(?:value\s*=\s*)?"([^"]*)"""")

    /** The two trees of the repository: Kotlin tests and Java tests. */
    fun defaultRoots(): List<Root> =
        listOf(
            Root(Tier.dir, "kotlin", ".kt", Tier.repoRoot),
            Root(Tier.javaDir, "java", ".java", Tier.repoRoot),
        )

    fun scan(roots: List<Root> = defaultRoots()): List<ScannedFile> =
        roots.filter { it.dir.isDirectory }.flatMap { root ->
            root.dir.walkTopDown()
                .onEnter { it.name != "support" && it.name != "offline" }
                .filter { it.isFile && it.name.endsWith(root.extension) && it.name != "package-info.java" }
                .sortedBy { it.path }
                .map { scanFile(it, root) }
                .toList()
        }

    fun scanFile(
        file: File,
        root: Root,
    ): ScannedFile {
        val all = file.readText().split("\n")
        val lines = all.dropLastWhile { it.isEmpty() }
        val rel = file.relativeTo(root.base).path.replace(File.separatorChar, '/')
        val kind =
            if (rel.contains("/samples/")) {
                "sample"
            } else {
                "test"
            }
        val problems = mutableListOf<Problem>()
        val regions = RegionScanner(lines, rel, problems).scan()
        return ScannedFile(file, rel, root.docset, kind, lines, regions, problems, scanMethods(lines, root.docset))
    }

    /** The state machine over the marker lines of one file. */
    private class RegionScanner(
        private val lines: List<String>,
        private val rel: String,
        private val problems: MutableList<Problem>,
    ) {
        private class Open(val id: String, val start: Int, val spaceless: Boolean, val origin: String?) {
            val hidden = mutableListOf<Pair<Int, Int?>>()
            var hideLine: Int? = null
            val visible = mutableListOf<Pair<Int, String>>()
        }

        private val regions = mutableListOf<Region>()
        private var open: Open? = null

        private fun problem(
            line: Int,
            rule: String,
            message: String,
        ) {
            problems.add(Problem(rel, line, rule, message))
        }

        private fun close(endLine: Int) {
            val o = open ?: return
            o.hideLine?.let {
                problem(it, "M3", "snippet.hide of \"${o.id}\" has no snippet.show")
                o.hidden.add(it to null)
            }
            regions.add(Region(o.id, o.start, endLine, o.hidden, o.visible, o.spaceless, o.origin))
            open = null
        }

        private fun plainLine(
            number: Int,
            text: String,
        ) {
            if (anyMention.containsMatchIn(text)) {
                problem(number, "M1", "comment mentions snippet. but is not a valid marker: ${text.trim()}")
            }
            val o = open
            if (o != null && o.hideLine == null && !blueprint.matches(text)) {
                o.visible.add(number to text)
            }
        }

        private fun hide(number: Int) {
            val o = open
            if (o == null || o.hideLine != null) {
                problem(number, "M3", "snippet.hide outside a snippet or inside a hidden block")
            } else {
                o.hideLine = number
            }
        }

        private fun show(number: Int) {
            val o = open
            val hideLine = o?.hideLine
            if (o == null || hideLine == null) {
                problem(number, "M3", "snippet.show without snippet.hide")
            } else {
                o.hidden.add(hideLine to number)
                o.hideLine = null
            }
        }

        private fun start(
            index: Int,
            id: String,
            spaceless: Boolean,
        ) {
            val number = index + 1
            val o = open
            if (o != null) {
                problem(number, "M2", "start marker \"$id\" inside the open snippet \"${o.id}\" (the docs extractor cuts the outer one)")
                close(number)
            }
            val origin = lines.getOrNull(index - 1)?.let { originLine.matchEntire(it)?.groupValues?.get(1) }
            open = Open(id, number, spaceless, origin)
        }

        fun scan(): List<Region> {
            for ((index, text) in lines.withIndex()) {
                val number = index + 1
                val m = marker.matchEntire(text)
                if (m == null) {
                    plainLine(number, text)
                    continue
                }
                val id = m.groupValues[2]
                when (id) {
                    "end" -> {
                        if (open == null) {
                            problem(number, "M2", "snippet.end without a start marker")
                        } else {
                            close(number)
                        }
                    }
                    "hide" -> hide(number)
                    "show" -> show(number)
                    else -> start(index, id, m.groupValues[1].isEmpty())
                }
            }
            open?.let {
                problem(it.start, "M2", "snippet \"${it.id}\" has no snippet.end")
                close(lines.size + 1)
            }
            return regions
        }
    }

    /** Counts braces of one line, skipping strings, characters and comments. The state carries block comments and raw strings. */
    private class BraceCounter {
        private var inBlockComment = false
        private var inRawString = false

        /** Returns the change of the brace depth and whether the line opens a brace. */
        fun delta(line: String): Pair<Int, Boolean> {
            var depth = 0
            var sawOpen = false
            var i = 0
            while (i < line.length) {
                val c = line[i]
                if (inBlockComment) {
                    if (line.startsWith("*/", i)) {
                        inBlockComment = false
                        i++
                    }
                } else if (inRawString) {
                    if (line.startsWith("\"\"\"", i)) {
                        inRawString = false
                        i += 2
                    }
                } else if (line.startsWith("//", i)) {
                    break
                } else if (line.startsWith("/*", i)) {
                    inBlockComment = true
                    i++
                } else if (line.startsWith("\"\"\"", i)) {
                    inRawString = true
                    i += 2
                } else if (c == '"' || c == '\'') {
                    i = skipQuoted(line, i, c)
                } else if (c == '{') {
                    depth++
                    sawOpen = true
                } else if (c == '}') {
                    depth--
                }
                i++
            }
            return depth to sawOpen
        }

        private fun skipQuoted(
            line: String,
            from: Int,
            quote: Char,
        ): Int {
            var i = from + 1
            while (i < line.length) {
                if (line[i] == '\\') {
                    i++
                } else if (line[i] == quote) {
                    return i
                }
                i++
            }
            return line.length
        }
    }

    private fun methodName(
        line: String,
        docset: String,
    ): String? {
        if (docset == "kotlin") {
            return kotlinFun.find(line)?.groupValues?.get(1)
        }
        if (javaNotMethod.containsMatchIn(line) || line.trimEnd().endsWith(";")) {
            return null
        }
        return javaMethod.find(line)?.groupValues?.get(1)
    }

    /** The line index where the method ends: the closing brace, or the end of the signature of a body-less method. */
    private fun methodEnd(
        lines: List<String>,
        from: Int,
    ): Int {
        val counter = BraceCounter()
        var depth = 0
        var opened = false
        var end = from
        var cursor = from
        while (cursor < lines.size) {
            val (delta, sawOpen) = counter.delta(lines[cursor])
            depth += delta
            opened = opened || sawOpen
            end = cursor
            if (opened && depth <= 0) {
                break
            }
            val signatureEnded = cursor > from && !lines[cursor].contains("(") && lines[cursor].trimEnd().endsWith(")")
            if (!opened && signatureEnded) {
                break
            }
            cursor++
        }
        return end
    }

    private fun scanMethods(
        lines: List<String>,
        docset: String,
    ): List<Method> {
        val methods = mutableListOf<Method>()
        for (index in lines.indices) {
            val name = methodName(lines[index], docset) ?: continue
            var annotationStart = index
            var isTest = false
            var compileOnly: String? = null
            var back = index - 1
            while (back >= 0 && lines[back].trim().startsWith("@")) {
                if (testAnnotation.containsMatchIn(lines[back])) {
                    isTest = true
                }
                compileOnlyAnnotation.find(lines[back])?.let { compileOnly = it.groupValues[1] }
                annotationStart = back
                back--
            }
            methods.add(Method(name.trim(), annotationStart + 1, index + 1, methodEnd(lines, index) + 1, isTest, compileOnly))
        }
        return methods
    }

    private fun contains(
        method: Method,
        line: Int,
    ) = line in method.declLine..method.endLine

    /**
     * A region that opens on the line right above a method (comments and blank lines only in between) and spans the whole
     * method: a Java helper that the docs show with its signature, for example a paging function that a test calls.
     */
    private fun spans(
        file: ScannedFile,
        method: Method,
        region: Region,
    ) = region.startLine < method.startLine && region.endLine >= method.endLine &&
        (region.startLine + 1 until method.startLine).all { file.lines[it - 1].isBlank() || file.lines[it - 1].trim().startsWith("//") }

    private fun snippetOf(
        file: ScannedFile,
        region: Region,
    ): Snippet {
        if (file.kind == "sample") {
            return Snippet(region.id, file, region, "sample", null, null)
        }
        val containing = file.methods.filter { contains(it, region.startLine) || spans(file, it, region) }
        val compileOnly = containing.filter { it.compileOnly != null }.minByOrNull { it.declLine }
        if (compileOnly != null) {
            return Snippet(region.id, file, region, "compile-only", compileOnly, null)
        }
        val test = containing.filter { it.isTest }.minByOrNull { it.declLine }
        if (test != null) {
            return Snippet(region.id, file, region, "test", test, test)
        }
        val holder = containing.maxByOrNull { it.declLine }
        val caller = holder?.let { h -> file.methods.firstOrNull { it.isTest && callsInside(file, it, h.name) } }
        if (holder != null && caller != null) {
            return Snippet(region.id, file, region, "helper", holder, caller)
        }
        return Snippet(region.id, file, region, "none", holder, null)
    }

    /** Maps every region of every file to the test that runs it. */
    fun snippets(files: List<ScannedFile>): List<Snippet> = files.flatMap { file -> file.regions.map { snippetOf(file, it) } }

    internal fun callsInside(
        file: ScannedFile,
        method: Method,
        name: String,
    ): Boolean {
        val call = Regex("""\b${Regex.escape(name)}\s*\(""")
        return (method.declLine + 1..method.endLine).any { call.containsMatchIn(file.lines[it - 1]) }
    }

    /**
     * The test methods that run a "Main" sample: methods of the test files in the sample's area folder (the parent of
     * `samples/`) that name the sample file's class or its package.
     */
    fun samplesRunners(
        files: List<ScannedFile>,
        sample: ScannedFile,
    ): List<Pair<ScannedFile, Method>> {
        val base = sample.path.nameWithoutExtension
        val packageLine = sample.lines.firstOrNull { it.trim().startsWith("package ") }
        val pkg = packageLine?.trim()?.removePrefix("package ")?.removeSuffix(";")?.trim()
        val area = sample.path.parentFile.parentFile
        val named = Regex("""\b${Regex.escape(base)}(?:Kt)?\b""")
        return files.filter { it.kind == "test" && it.path.parentFile == area }.flatMap { tf ->
            val runners =
                tf.methods.filter { it.isTest }.filter { m ->
                    (m.declLine..m.endLine).any { line ->
                        val text = tf.lines[line - 1]
                        named.containsMatchIn(text) || (pkg != null && text.contains("$pkg."))
                    }
                }
            runners.map { tf to it }
        }
    }

    /** The key roles a text names: `"open"`, `"pam"`, `"datasync"` as string literals. */
    fun rolesIn(text: String): List<String> = listOf("open", "pam", "datasync").filter { text.contains("\"$it\"") }
}
