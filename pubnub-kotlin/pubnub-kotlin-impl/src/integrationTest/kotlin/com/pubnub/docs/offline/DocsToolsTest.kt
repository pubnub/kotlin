package com.pubnub.docs.offline

import com.google.gson.JsonParser
import com.pubnub.api.PubNubException
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.docs.support.Env
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Redact
import com.pubnub.docs.support.Resources
import com.pubnub.docs.support.Retry
import com.pubnub.docs.support.Tier
import com.pubnub.docs.support.Wait
import com.pubnub.docs.support.tools.DocsCheck
import com.pubnub.docs.support.tools.Equivalence
import com.pubnub.docs.support.tools.Extractor
import com.pubnub.docs.support.tools.Goldens
import com.pubnub.docs.support.tools.Groups
import com.pubnub.docs.support.tools.LintMarkers
import com.pubnub.docs.support.tools.LintSelfTest
import com.pubnub.docs.support.tools.Manifest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.util.function.Consumer

/**
 * The offline checks of the live docs tier. No keys, no network. Run them with `scripts/dev/docs-snippets.sh lint`.
 *
 * Marker lint and its self test, manifest, groups, exceptions file, the extractor proof against the TypeScript output,
 * the equivalence report, the docs cross-check and the support helpers that can be tested without a keyset.
 */
class DocsToolsTest {
    /** The commit of origin/master the goldens were made from. Its docs module files are read with `git show`. */
    private val sourceCommit = "78e6a311841eb7d4290ac18af3903d44bc51a09d"

    @Test
    fun markerLintOfTheTierIsClean() {
        val result = LintMarkers.lint()
        result.warnings.forEach { println("lint: warning $it") }
        result.notes.forEach { println("lint: note $it") }
        println(LintMarkers.summary(result))
        assertTrue(result.errors.isEmpty(), "lint errors:\n" + result.errors.joinToString("\n"))
    }

    @Test
    fun markerLintSelfTestReportsEveryBrokenFile() {
        val failures = LintSelfTest.run()
        println("lint self test: ${LintSelfTest.caseCount()} broken files and 1 clean file")
        assertTrue(failures.isEmpty(), "lint self test failed:\n" + failures.joinToString("\n"))
    }

    @Test
    fun manifestIsUpToDate() {
        val problems = Manifest.check()
        assertTrue(problems.isEmpty(), "manifest problems:\n" + problems.joinToString("\n"))
    }

    @Test
    fun exceptionsFileHasTheRightShape() {
        val problems = Manifest.checkExceptions()
        assertTrue(problems.isEmpty(), "snippet-exceptions.json problems:\n" + problems.joinToString("\n"))
    }

    @Test
    fun groupsCoverEveryTestClassExactlyOnce() {
        val problems = Groups.check()
        println("groups: ${Groups.load().joinToString { it.name }}")
        assertTrue(problems.isEmpty(), "groups problems:\n" + problems.joinToString("\n"))
    }

    @Test
    fun groupsCheckDetectsBrokenGroups() {
        val broken =
            listOf(
                Groups.Group("a", listOf("open"), listOf("x.Missing"), emptyList()),
                Groups.Group("a", listOf("nope"), emptyList(), emptyList()),
            )
        val found = Groups.check(broken, emptyList(), null)
        assertTrue(found.any { "twice" in it }, "a repeated group name is not detected: $found")
        assertTrue(found.any { "unknown role" in it }, "an unknown role is not detected: $found")
        assertTrue(found.any { "not a test class" in it }, "a missing class is not detected: $found")
    }

    @Test
    fun extractorEqualsTheTypeScriptFunctionOnEveryFixture() {
        val root = JsonParser.parseString(Tier.file("extractor-fixtures.json").readText()).asJsonObject
        val cases = root.getAsJsonArray("cases")
        val different = mutableListOf<String>()
        for (case in cases) {
            val c = case.asJsonObject
            val ids = c.getAsJsonArray("ids").map { it.asString }
            val prefixes = c.getAsJsonArray("prefixes").map { it.asString }
            val expected = c.getAsJsonArray("expected").map { it.asString }
            val actual = Extractor.extractSnippets(c.get("text").asString, ids, prefixes)
            if (actual != expected) {
                different.add("${c.get("name").asString}: expected $expected, port returned $actual")
            }
        }
        println("extractor: ${cases.size()} fixtures, ${cases.size() - different.size} equal to the TypeScript output")
        assertTrue(cases.size() >= 40, "the fixture table is too small: ${cases.size()}")
        assertTrue(different.isEmpty(), "the port differs from the docs extractor:\n" + different.joinToString("\n"))
    }

    private fun gitShow(path: String): String {
        val process = ProcessBuilder("git", "-C", Tier.repoRoot.path, "show", "$sourceCommit:$path").start()
        val text = process.inputStream.readBytes().toString(Charsets.UTF_8)
        val error = process.errorStream.readBytes().toString(Charsets.UTF_8)
        check(process.waitFor() == 0) {
            "git show $sourceCommit:$path failed ($error). The goldens need the history of the repository, fetch it with full depth."
        }
        return text
    }

    @Test
    fun extractorEqualsTheTypeScriptFunctionOnEveryGolden() {
        val goldens = Goldens.load()
        val sources = HashMap<String, String>()
        val different = mutableListOf<String>()
        var variants = 0
        for ((key, golden) in goldens) {
            val path = key.substringBefore('#')
            val id = key.substringAfter('#')
            val text = sources.getOrPut(path) { gitShow(path) }
            if (Extractor.region(text, id, golden.prefixes) != golden.text) {
                different.add("$key: port output differs from the TypeScript output")
            }
            for (other in golden.otherPrefixes) {
                variants++
                if (Extractor.region(text, id, other) != golden.text) {
                    different.add("$key: port output differs for the prefixes $other")
                }
            }
        }
        val equal = goldens.size - different.size
        val counts = "${goldens.size} goldens from ${sources.size} files (plus $variants prefix variants)"
        println("extractor: $counts, $equal equal to the TypeScript output")
        assertEquals(463, goldens.size, "432 embedded pairs and 31 orphan ids")
        assertTrue(different.isEmpty(), "the port differs from the docs extractor:\n" + different.joinToString("\n"))
    }

    @Test
    fun equivalenceReportsTheGoldensNotYetPorted() {
        val report = Equivalence.compare()
        println(report.summary())
        val problems = Equivalence.problems(report)
        assertTrue(problems.isEmpty(), "equivalence problems:\n" + problems.joinToString("\n"))
        assertEquals(report.total, report.identical.size + report.differ.size + report.notPorted.size)
    }

    @Test
    fun equivalenceComparesAMappedRegion() {
        val dir = Files.createTempDirectory("docs-equivalence").toFile()
        try {
            File(dir, "new").mkdirs()
            File(dir, "new/A.kt").writeText("fun a() {\n    // snippet.one\n    val x = 1\n    // snippet.end\n}\n")
            val golden = Goldens.Golden("kotlin", Extractor.DEFAULT_PREFIXES, emptyList(), "val x = 1")
            val other = Goldens.Golden("kotlin", Extractor.DEFAULT_PREFIXES, emptyList(), "val x = 2")
            val manifest = JsonParser.parseString("""{"snippet-map": {"old/A.kt#one": "new/A.kt#one"}}""").asJsonObject
            val same = Equivalence.compare(mapOf("old/A.kt#one" to golden, "old/C.kt#three" to golden), manifest, dir)
            assertEquals(listOf("old/A.kt#one"), same.identical)
            assertEquals(listOf("old/C.kt#three"), same.notPorted)
            val changed = Equivalence.compare(mapOf("old/A.kt#one" to other), manifest, dir)
            assertEquals(1, changed.differ.size)
            assertEquals(1, Equivalence.problems(changed, strict = false).size)
            assertEquals(1, Equivalence.problems(same, strict = true).size)
        } finally {
            dir.deleteRecursively()
        }
    }

    @Test
    fun docsCheckClassifiesEmbeds() {
        val dir = Files.createTempDirectory("docs-check").toFile()
        try {
            val page = File(dir, "docs/sdks/kotlin/api-reference/x.md").also { it.parentFile.mkdirs() }
            val base = "https://raw.githubusercontent.com/pubnub/kotlin/refs/heads"
            page.writeText(
                "<EmbeddedCode url={'$base/master/old/A.kt'} language={'kotlin'} snippets={['one','two']} showLineNumbers={true} />\n" +
                    "<EmbeddedCode url={'$base/master/old/res.xml'} language={'xml'} showLineNumbers={true} />\n" +
                    "<EmbeddedCode url={'$base/other/old/A.kt'} language={'kotlin'} snippets={['one']} />\n",
            )
            val manifest =
                JsonParser.parseString(
                    """{"files": {"new/A.kt": {"one": {"mode": "live", "test": "t"}}}, "whole_files": {}, "snippet-map": {"old/A.kt#one": "new/A.kt#one"}}""",
                ).asJsonObject
            val exceptions = File(dir, "exceptions.json")
            exceptions.writeText("""{"exceptions": [{"path": "old/res.xml", "reason": "a resource file, not code"}]}""")
            val results = DocsCheck.classify(dir, null, manifest, exceptions, dir)
            assertEquals(listOf("live", "NOT COVERED", "exception", "NOT COVERED"), results.map { it.status })
            assertTrue(results[0].why.startsWith("repoint pending"))
        } finally {
            dir.deleteRecursively()
        }
    }

    @Test
    fun docsCheckReportOnlyAgainstTheDocsCheckout() {
        val root = DocsCheck.docsRepo()
        if (root == null) {
            println("docs-check: DOCS_REPO is not set, the report needs a documentation checkout")
            return
        }
        val results = DocsCheck.classify(root)
        val notCovered = DocsCheck.report(root)
        assertTrue(results.isNotEmpty(), "no EmbeddedCode tag of pubnub/kotlin was found in ${root.name}")
        assertEquals(results.count { it.status == "NOT COVERED" }, notCovered)
    }

    @Test
    fun envReportsOnlySetOrMissing() {
        Env.load()
        for (line in Env.status()) {
            val shape = Regex("""^(kotlin|java) [a-z]+: (set|missing: [A-Z_, ]+)$""")
            assertTrue(shape.matches(line), "an env status line must name the SDK, roles and env names only")
        }
        assertEquals(setOf("open", "pam", "datasync", "admin"), Env.roles.keys)
        assertFalse(Env.keysetIds.isEmpty())
        assertTrue(Env.missing("open").all { it in Env.roles.getValue("open").map(Env::actualName) })
        // The Kotlin task runs with docs.sdk=kotlin: plain names. JavaSupportTest checks the JAVA_ names.
        assertEquals("kotlin", Env.sdk)
        assertEquals("PUBLISH_KEY", Env.actualName("PUBLISH_KEY"))
    }

    @Test
    fun redactMasksRegisteredValuesAndShapes() {
        Redact.register("sub-c-12345678-aaaa-bbbb-cccc-1234567890ab")
        Redact.register("a-token-value-that-is-registered")
        val text =
            "url ?auth=somesecret&x=1 key sub-c-12345678-aaaa-bbbb-cccc-1234567890ab and a-token-value-that-is-registered " +
                "and pub-c-12345678-aaaa-bbbb-cccc-1234567890zz"
        val scrubbed = Redact.scrub(text)
        assertEquals(0, Redact.leakCount(scrubbed), "a registered value survived the scrub")
        assertFalse(scrubbed.contains("somesecret"))
        assertFalse(scrubbed.contains("pub-c-1234"))
    }

    @Test
    fun waitHelpersRetryAndExplainTimeouts() {
        var calls = 0
        val result =
            Wait.eventually("a value", timeoutMs = 3000, intervalMs = 10) {
                calls++
                if (calls < 3) {
                    throw AssertionError("not yet")
                }
                "done"
            }
        assertEquals("done", result)
        assertEquals(3, calls)

        var passes = 0
        Wait.settled("three in a row", timeoutMs = 3000, intervalMs = 10) {
            passes++
            if (passes == 2) {
                throw AssertionError("a flaky pass")
            }
        }
        assertEquals(5, passes, "the pass counter must restart after a failure: 1, fail, then 3 in a row")

        val error =
            assertThrows(AssertionError::class.java) {
                Wait.eventually("the thing", timeoutMs = 60, intervalMs = 10) {
                    throw AssertionError("saw 2 of 3")
                }
            }
        val message = error.message.orEmpty()
        assertTrue(message.contains("the thing") && message.contains("saw 2 of 3"), "the timeout message must say what was seen: $message")
    }

    @Test
    fun retryClassifiesTransientFailures() {
        assertTrue(Retry.isTransient(PubNubException("server error", 503, null)))
        assertTrue(Retry.isTransient(PubNubException("too many", 429, null)))
        assertFalse(Retry.isTransient(PubNubException("forbidden", 403, null)))
        assertFalse(Retry.isTransient(AssertionError("an assertion")))
        assertTrue(Retry.isTransient(PubNubException("connect", 0, java.net.SocketTimeoutException("timeout"))))
        var calls = 0
        val value =
            Retry.retryTransient(attempts = 3, pauseMs = 1) {
                calls++
                if (calls < 3) {
                    throw PubNubException("again", 500, null)
                }
                "ok"
            }
        assertEquals("ok", value)
        calls = 0
        assertThrows(PubNubException::class.java) {
            Retry.retryTransient(attempts = 3, pauseMs = 1) {
                calls++
                throw PubNubException("no", 400, null)
            }
        }
        assertEquals(1, calls, "a 4xx must not be retried")
    }

    // A tiny fake of the client shape, to prove the recorder without a network.
    interface FakeEndpoint {
        fun channel(name: String): FakeEndpoint

        fun async(callback: Consumer<Result<String>>)

        @Throws(PubNubException::class)
        fun sync(): String
    }

    interface FakeClient {
        fun publish(channel: String): FakeEndpoint
    }

    private class FakeImpl(val fail: Boolean, val seen: MutableList<String>) : FakeClient {
        override fun publish(channel: String): FakeEndpoint = endpoint(channel)

        private fun endpoint(channel: String): FakeEndpoint =
            object : FakeEndpoint {
                override fun channel(name: String): FakeEndpoint {
                    seen.add(name)
                    return endpoint(name)
                }

                override fun async(callback: Consumer<Result<String>>) {
                    seen.add("async:$channel")
                    if (fail) {
                        callback.accept(Result.failure(PubNubException("boom", 500, null)))
                    } else {
                        callback.accept(Result.success("sent"))
                    }
                }

                override fun sync(): String {
                    if (fail) {
                        throw PubNubException("boom", 500, null)
                    }
                    return "sent"
                }
            }
    }

    @Test
    fun recorderRecordsAsyncAndSyncAndSwapsPlaceholders() {
        val seen = mutableListOf<String>()
        val recorder = Recorder().placeholder("someChannel", "live-channel")
        val client = recorder.wrap(FakeImpl(false, seen), FakeClient::class.java)
        var sampleSawResult = false
        client.publish("someChannel").channel("someChannel").async { result -> sampleSawResult = result.isSuccess }
        assertTrue(sampleSawResult, "the sample's own callback must still run")
        assertEquals("sent", recorder.awaitOk("publish"))
        assertEquals(listOf("live-channel", "async:live-channel"), seen)
        assertEquals("sent", client.publish("x").sync())
        assertEquals(2, recorder.records("publish").size)

        val failing = Recorder()
        val broken = failing.wrap(FakeImpl(true, mutableListOf()), FakeClient::class.java)
        var callbackRan = false
        // The sample swallows the error in an empty failure block. The recorder still sees it.
        broken.publish("c").async { result ->
            result.onFailure { }
            callbackRan = true
        }
        assertTrue(callbackRan)
        assertTrue(failing.awaitFailure("publish") is PubNubException)
        assertThrows(AssertionError::class.java) { failing.assertNoFailures() }
        assertThrows(PubNubException::class.java) { broken.publish("c").sync() }
        assertEquals(2, failing.records("publish").count { !it.success })
    }

    @Test
    fun resourcesListEveryAreaOfTheGroups() {
        assertEquals(Groups.load().map { it.name }.toSet(), Resources.areas.keys, "every group has one resources area of the same name")
        assertEquals(Resources.allNames(), Resources.areas.values.flatMap { it.names() }.toSet())
    }
}
