package com.pubnub.docs.support.tools

import com.pubnub.docs.support.Resources

/**
 * Marker lint of the live docs tier. Static: no keys, no network, nothing is executed. The docs extractor defines what a
 * marker is (see [Extractor]).
 *
 * Marker rules
 * - M1 syntax `// snippet.<id>` (a start marker without the space is a warning, three legacy files keep it), id charset
 *   `[a-zA-Z0-9_-]`, no comment that mentions `snippet.` without being a marker
 * - M2 every snippet has `snippet.end`, no start marker inside an open snippet
 * - M3 `snippet.hide` has `snippet.show`, a hidden block has at most [MAX_HIDDEN_LINES] lines and holds code
 * - M4 an id appears once per docset (the docs address a snippet by file and id, a second copy drifts)
 * - M5 a snippet is not empty
 * - M6 no test-only code or support names in the visible text
 * - M7 no try, catch or printStackTrace in the visible text, except the ids in [ERROR_HANDLING_SNIPPETS]
 * - R7 every literal channel, channel group or user id in a region is listed in [Resources]
 *
 * Honesty rules
 * - H1 a snippet runs inside a test method, or in a helper that a test method calls (or is a "Main" sample, see H5)
 * - H2 an assertion follows the snippet's end line in the same test method (or its helper), or the method carries
 *   `// lint: asserted-via <helper>`
 * - H3 no `return` inside the snippet that belongs to the method holding it
 * - H4 no `@Disabled`, `@Ignore`, `Assumptions` and the like in a tier file
 * - H5 a "Main" sample (a file in `samples/` with regions) is run by a test method of the same area that names it and
 *   asserts after it
 */
object LintMarkers {
    const val MAX_HIDDEN_LINES = 3

    /** Snippets that show error handling on purpose (M7 does not apply). Pairs of docset and id. Keep it short. */
    val ERROR_HANDLING_SNIPPETS: Set<Pair<String, String>> =
        setOf(
            // publish and subscribe: the published sample prints the error with printStackTrace
            "kotlin" to "publishWithMetadata",
            "kotlin" to "publishJsonArrayGSON",
            "kotlin" to "publishJsonObjectOrgJson",
            "kotlin" to "publishJsonArrayOrgJson",
            "kotlin" to "publishAndStoreMessage10Hours",
            "kotlin" to "fireBasic",
            "kotlin" to "signalBasic",
            "java" to "signalMessage",
            // access manager: the published "Main" sample prints the stack trace of a failed grant
            "kotlin" to "grantTokenMain",
            // messaging pages: the published samples print the stack trace or catch the exception on purpose
            "kotlin" to "addChannelsToChannelGroupMain",
            "kotlin" to "filesBasicUsage",
            "kotlin" to "messageReactionsMain",
            "kotlin" to "getStateForUuid",
            "java" to "getMessageActionsBasic",
            "java" to "historyPagingExampleDeprecated",
            "java" to "FetchMessagesWithPagingApp",
            "java" to "getMessageActionsWithPagingApp",
            // landing pages: the Kotlin publish step prints the stack trace, the Java app catches the sleep and the file sample catches all
            "kotlin" to "publishMessagesBasic",
            "java" to "addDelay",
            "java" to "sendFileApp",
            // getting started: the published Kotlin logging samples catch the exception on purpose
            "kotlin" to "loggingLog4j2Main",
            "kotlin" to "loggingLogbackMain",
            "kotlin" to "loggingSlf4jSimpleMain",
        )

    /** Hidden blocks that may be longer than [MAX_HIDDEN_LINES], each with its reason. Empty: every hidden block is short. */
    val LONG_HIDDEN_BLOCKS: Set<Pair<String, String>> = emptySet()

    /**
     * Snippets whose published text holds assertions of its own (M6 does not apply). Pairs of docset and id. Keep it short.
     * The sample ends with `System.exit(0)` and checks its own result with assertions, a docs finding.
     */
    val ASSERTIONS_IN_DOCS_SNIPPETS: Set<Pair<String, String>> =
        setOf(
            "java" to "iterativelyUpdateExistingMetadataMain",
            "kotlin" to "iterativelyUpdateExistingMetadataMain",
        )

    private val assertion =
        Regex(
            """\b(assert[A-Z]\w*|assertThat|fail|awaitOk|awaitFailure|eventually|eventuallyRun|settled|settledRun|expectEvent|connected)""" +
                """\s*\(|\bAssertions\.|\bAssert\.""",
        )
    private val escape = Regex("""^\s*//\s*lint:\s*asserted-via\s+(\w+)\s*$""")
    private val testOnly =
        Regex(
            """\b(assert[A-Z]\w*|Assertions|Assert|Wait|eventually|settled|expectEvent|sleepToProve|LiveClients|LiveKeys|""" +
                """Recorder|Cleanup|Resources|Redact|Retry|retryTransient|Env|Janitor|Preflight|LiveExtension)\b""" +
                """|com\.pubnub\.docs\.support|com\.pubnub\.test\.|org\.junit|kotlin\.test""",
        )
    private val lineComment = Regex("""(?<!:)//.*$""")
    private val errorHandling = Regex("""\btry\s*[({]|\bcatch\s*\(|\bprintStackTrace\b|\bfinally\b""")
    private val skipping =
        Regex(
            """@Disabled|@Ignore|\bAssumptions\b|\bAssume\b|@EnabledIf|@DisabledIf|@EnabledOn|@DisabledOn|@EnabledFor|@DisabledFor""",
        )
    private val returnStatement = Regex("""\breturn\b(?!@)""")
    private val isolatedScope = Regex("""->|\bobject\s*:|\bnew\s+\w[\w.<>]*\s*\(|\bfun\s*\(""")

    // Name carrying literals: UserId("x"), uuid("x"), channel("x"), channels = listOf("a", "b"), channelGroup = "g" and so on.
    private const val NAME_KEYS = """(?:channels?|channelGroups?|groups?|uuids?|userIds?|users?|UserId|channelId|channelNames?)"""
    private val singleName = Regex("""\b$NAME_KEYS\s*(?:\(\s*|=\s*)"([^"\n]+)"""")
    private val listName =
        Regex(
            """\b$NAME_KEYS\s*(?:\(\s*|=\s*)""" +
                """(?:listOf|setOf|arrayListOf|mutableListOf|Arrays\.asList|List\.of|Set\.of|Collections\.singletonList)\(([^)]*)\)""",
        )
    private val literal = Regex(""""([^"\n]*)"""")

    class Result(
        val errors: List<String>,
        val warnings: List<String>,
        val notes: List<String>,
        val files: Int,
        val snippets: Int,
    )

    private fun directReturns(visible: List<Pair<Int, String>>): List<Int> {
        val found = mutableListOf<Int>()
        val scopes = ArrayDeque<Boolean>()
        for ((number, text) in visible) {
            val code = text.substringBefore("//")
            val isolated = isolatedScope.containsMatchIn(code)
            if (returnStatement.containsMatchIn(code) && scopes.lastOrNull() != true && !isolated) {
                found.add(number)
            }
            for (c in code) {
                if (c == '{') {
                    scopes.addLast(isolated || scopes.lastOrNull() == true)
                } else if (c == '}' && scopes.isNotEmpty()) {
                    scopes.removeLast()
                }
            }
        }
        return found
    }

    private fun literalNames(text: String): List<String> {
        val found = mutableListOf<String>()
        singleName.findAll(text).forEach { found.add(it.groupValues[1]) }
        listName.findAll(text).forEach { match -> literal.findAll(match.groupValues[1]).forEach { found.add(it.groupValues[1]) } }
        return found
    }

    private fun lintRegion(
        file: SnippetScan.ScannedFile,
        region: SnippetScan.Region,
        resources: Set<String>,
        errors: MutableList<String>,
        warnings: MutableList<String>,
    ) {
        val where = "${file.rel}:${region.startLine}"
        if (region.spaceless) {
            val hint = "only a docs tag with prefixes={['//']} renders it"
            warnings.add("$where: M1 start marker \"${region.id}\" has no space after the slashes ($hint)")
        }
        if (region.visible.none { it.second.isNotBlank() }) {
            errors.add("$where: M5 snippet \"${region.id}\" is empty")
        }
        for ((hide, show) in region.hidden) {
            val end = show ?: region.endLine
            val size = end - hide - 1
            if (size > MAX_HIDDEN_LINES && (file.docset to region.id) !in LONG_HIDDEN_BLOCKS) {
                errors.add("${file.rel}:$hide: M3 hidden block of \"${region.id}\" has $size lines (max $MAX_HIDDEN_LINES)")
            }
            val holdsCode = (hide + 1 until end).any { file.lines[it - 1].isNotBlank() && !file.lines[it - 1].trim().startsWith("//") }
            if (!holdsCode) {
                errors.add("${file.rel}:$hide: M3 hidden block of \"${region.id}\" holds no code")
            }
        }
        for ((number, original) in region.visible) {
            // A comment may use any word ("Wait for the operation"), only code can be test-only code.
            val text = original.replace(lineComment, "")
            testOnly.find(text)?.takeIf { (file.docset to region.id) !in ASSERTIONS_IN_DOCS_SNIPPETS }?.let {
                errors.add("${file.rel}:$number: M6 test-only name \"${it.value}\" inside snippet \"${region.id}\"")
            }
            if (errorHandling.containsMatchIn(text) && (file.docset to region.id) !in ERROR_HANDLING_SNIPPETS) {
                errors.add("${file.rel}:$number: M7 error handling inside snippet \"${region.id}\" (not in ERROR_HANDLING_SNIPPETS)")
            }
            literalNames(original).filter { it !in resources }.forEach {
                errors.add("${file.rel}:$number: R7 literal name \"$it\" in snippet \"${region.id}\" is not listed in Resources")
            }
        }
    }

    private fun hasAssertion(
        file: SnippetScan.ScannedFile,
        lines: IntRange,
    ): Boolean = lines.any { assertion.containsMatchIn(file.lines[it - 1]) }

    private fun lintOwner(
        s: SnippetScan.Snippet,
        errors: MutableList<String>,
        notes: MutableList<String>,
    ) {
        val file = s.file
        val region = s.region
        if (s.owner == "none") {
            val where = "${file.rel}:${region.startLine}"
            errors.add("$where: H1 snippet \"${s.id}\" is not inside a test method or a helper that a test method calls")
            return
        }
        if (s.owner == "sample") {
            return
        }
        if (s.owner == "compile-only") {
            notes.add("compile-only: ${file.rel} ${s.id} (${s.holder?.compileOnly})")
            return
        }
        val holder = s.holder!!
        val test = s.test!!
        for (number in directReturns(region.visible)) {
            errors.add("${file.rel}:$number: H3 return inside snippet \"${s.id}\" can skip the rest of ${holder.name}")
        }
        val inTest = test.declLine + 1..test.endLine
        val checked = hasAssertion(file, region.endLine + 1..holder.endLine) || (s.owner == "helper" && hasAssertion(file, inTest))
        if (!checked) {
            val via = (test.declLine..test.endLine).firstNotNullOfOrNull { escape.matchEntire(file.lines[it - 1])?.groupValues?.get(1) }
            if (via != null) {
                notes.add("asserted-via $via: ${file.rel}::${test.name} (${s.id})")
            } else {
                errors.add("${file.rel}:${region.endLine}: H2 no assertion after snippet \"${s.id}\" in ${test.name}")
            }
        }
    }

    private fun lintSamples(
        files: List<SnippetScan.ScannedFile>,
        errors: MutableList<String>,
    ) {
        for (file in files.filter { it.kind == "sample" && it.regions.isNotEmpty() }) {
            val base = file.path.nameWithoutExtension
            val runners = SnippetScan.samplesRunners(files, file)
            if (runners.isEmpty()) {
                val area = file.path.parentFile.parentFile.name
                errors.add("${file.rel}:1: H5 \"Main\" sample $base is not run by a test method of $area/ that names it")
            } else if (runners.none { (tf, m) -> hasAssertion(tf, m.declLine..m.endLine) }) {
                errors.add("${file.rel}:1: H5 the test that runs $base does not assert after the call")
            }
        }
    }

    @JvmStatic
    @JvmOverloads
    fun lint(
        files: List<SnippetScan.ScannedFile> = SnippetScan.scan(),
        resources: Set<String> = Resources.allNames(),
    ): Result {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        val notes = mutableListOf<String>()
        val snippets = SnippetScan.snippets(files)

        for (file in files) {
            file.problems.forEach { errors.add(it.toString()) }
            for (match in skipping.findAll(file.text)) {
                val line = file.text.substring(0, match.range.first).count { it == '\n' } + 1
                errors.add("${file.rel}:$line: H4 ${match.value} is not allowed in the live tier")
            }
            file.regions.forEach { lintRegion(file, it, resources, errors, warnings) }
        }

        // M4: ids are unique per docset
        snippets.groupBy { it.file.docset to it.id }.forEach { (key, found) ->
            if (found.size > 1) {
                val at = found.joinToString(", ") { "${it.file.rel}:${it.region.startLine}" }
                val first = found[0]
                val where = "${first.file.rel}:${first.region.startLine}"
                errors.add("$where: M4 snippet id \"${key.second}\" appears ${found.size} times in the ${key.first} tree ($at)")
            }
        }
        snippets.forEach { lintOwner(it, errors, notes) }
        lintSamples(files, errors)

        val used = snippets.map { it.file.docset to it.id }.toSet()
        (LONG_HIDDEN_BLOCKS - used).forEach { warnings.add("allow-list LONG_HIDDEN_BLOCKS: $it is not needed any more") }
        (ERROR_HANDLING_SNIPPETS - used).forEach { warnings.add("allow-list ERROR_HANDLING_SNIPPETS: $it is not needed any more") }
        return Result(errors, warnings, notes, files.size, snippets.size)
    }

    @JvmStatic
    fun summary(result: Result): String {
        return "marker lint: ${result.files} files, ${result.snippets} snippets, ${result.errors.size} errors, ${result.warnings.size} warnings"
    }
}
