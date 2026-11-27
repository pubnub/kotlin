package com.pubnub.docs.support.tools

import java.io.File
import java.nio.file.Files

/**
 * Self test of the marker lint: writes deliberately broken files to a temp directory and checks that each defect is
 * reported with its rule, and that a clean file passes. Run by the offline tests. Needs no keys and no network.
 */
object LintSelfTest {
    private class Case(val name: String, val rule: String, val files: Map<String, String>)

    private val listed = setOf("listed-channel")

    private const val I8 = "        "
    private const val CHECK = "${I8}assertTrue(true)\n"

    private fun testClass(
        body: String,
        extra: String = "",
        annotations: String = "",
    ): String =
        "package com.pubnub.docs.messaging\n\nimport org.junit.jupiter.api.Test\n\nclass FixtureDocsTest {\n" +
            "    @Test\n$annotations    fun fixture() {\n$body    }\n$extra}\n"

    private fun region(inner: String) = "$I8// snippet.alpha\n$inner$I8// snippet.end\n"

    private fun lines(vararg code: String) = code.joinToString("") { "$I8$it\n" }

    private fun single(
        name: String,
        rule: String,
        file: String,
    ) = Case(name, rule, mapOf("a/FixtureDocsTest.kt" to file))

    private fun brokenMarkers(): List<Case> =
        listOf(
            single("M1 malformed marker", "M1", testClass(lines("// snippet.alpha beta", "val x = 1") + CHECK)),
            single("M2 missing end", "M2", testClass(lines("// snippet.alpha", "val x = 1") + CHECK)),
            single(
                "M2 nested start",
                "M2",
                testClass(lines("// snippet.alpha", "val x = 1", "// snippet.beta", "val y = 2", "// snippet.end") + CHECK),
            ),
            single("M3 hide without show", "M3", testClass(region(lines("// snippet.hide", "val x = 1")) + CHECK)),
            single(
                "M3 long hidden block",
                "M3",
                testClass(
                    region(lines("val a = 0", "// snippet.hide", "val b = 1", "val c = 1", "val d = 1", "val e = 1", "// snippet.show")) + CHECK,
                ),
            ),
            single(
                "M3 hidden block without code",
                "M3",
                testClass(region(lines("val a = 0", "// snippet.hide", "", "// a comment", "// snippet.show")) + CHECK),
            ),
            Case(
                "M4 duplicate id",
                "M4",
                mapOf(
                    "a/FixtureDocsTest.kt" to testClass(region(lines("val a = 0")) + CHECK),
                    "b/OtherDocsTest.kt" to testClass(region(lines("val b = 0")) + CHECK).replace("FixtureDocsTest", "OtherDocsTest"),
                ),
            ),
            single("M5 empty region", "M5", testClass(region("") + CHECK)),
        )

    private fun brokenCode(): List<Case> =
        listOf(
            single("M6 assertion in the visible text", "M6", testClass(region(lines("assertTrue(true)")) + CHECK)),
            single("M6 support name in the visible text", "M6", testClass(region(lines("val c = LiveClients()")) + CHECK)),
            single(
                "M7 try in the visible text",
                "M7",
                testClass(region(lines("try {", "    work()", "} catch (e: Exception) {", "}")) + CHECK),
            ),
            single("R7 literal not in Resources", "R7", testClass(region(lines("pubnub.channel(\"unlisted-channel\")")) + CHECK)),
            single(
                "H1 region outside a test",
                "H1",
                testClass(lines("// nothing"), extra = "\n    fun loose() {\n" + region(lines("val a = 0")) + "    }\n"),
            ),
            single("H2 no assertion after the region", "H2", testClass(region(lines("val a = 0")))),
            single("H3 return inside the region", "H3", testClass(region(lines("if (flag) return")) + CHECK)),
            single("H4 disabled test", "H4", testClass(region(lines("val a = 0")) + CHECK, annotations = "    @Disabled\n")),
            Case(
                "H5 Main sample that no test runs",
                "H5",
                mapOf(
                    "a/FixtureDocsTest.kt" to testClass(lines("// nothing")),
                    "a/samples/LonelyMain.kt" to
                        "package com.pubnub.docs.messaging.samples\n\n// snippet.mainsample\nfun main() {\n    val a = 0\n}\n// snippet.end\n",
                ),
            ),
        )

    private val cases: List<Case> = brokenMarkers() + brokenCode()

    private val clean =
        mapOf(
            "a/FixtureDocsTest.kt" to
                testClass(
                    lines(
                        "// snippet.alpha",
                        "// snippet.hide",
                        "keys()",
                        "// snippet.show",
                        "pubnub.channel(\"listed-channel\")",
                        "// snippet.end",
                    ) + CHECK,
                ),
        )

    private fun lintOf(files: Map<String, String>): LintMarkers.Result {
        val dir = Files.createTempDirectory("docs-lint-selftest").toFile()
        try {
            val tree = File(dir, "kotlin")
            files.forEach { (path, text) -> File(tree, path).also { it.parentFile.mkdirs() }.writeText(text) }
            val scanned = SnippetScan.scan(listOf(SnippetScan.Root(tree, "kotlin", ".kt", dir)))
            return LintMarkers.lint(scanned, listed)
        } finally {
            dir.deleteRecursively()
        }
    }

    /** Returns what went wrong. Empty when every broken file is reported and the clean file passes. */
    @JvmStatic
    fun run(): List<String> {
        val failures = mutableListOf<String>()
        for (case in cases) {
            val result = lintOf(case.files)
            if (result.errors.none { Regex("""\b${case.rule}\b""").containsMatchIn(it) }) {
                failures.add("${case.name}: rule ${case.rule} was not reported. Errors: ${result.errors}")
            }
        }
        val result = lintOf(clean)
        if (result.errors.isNotEmpty()) {
            failures.add("the clean file was rejected: ${result.errors}")
        }
        if (result.snippets != 1) {
            failures.add("the clean file should hold 1 snippet, found ${result.snippets}")
        }
        return failures
    }

    @JvmStatic
    fun caseCount(): Int = cases.size
}
