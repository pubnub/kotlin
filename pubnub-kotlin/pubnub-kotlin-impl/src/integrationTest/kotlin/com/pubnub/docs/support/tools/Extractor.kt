package com.pubnub.docs.support.tools

import java.util.regex.Pattern

/**
 * Exact port of `extractSnippets` and `findMissingSnippetIds`, the functions of the docs site that render every
 * `<EmbeddedCode>` tag. Including the `prefixes` option, hide and show,
 * blueprint lines and the removal of the common indentation.
 *
 * The port keeps the quirks of the original on purpose, because the tools must see what readers see: the snippet id is put
 * into a regular expression without escaping, only the first start marker of an id counts, a nested start marker ends the
 * outer snippet, `//snippet.a` without a space is no marker unless the prefix is `//`, and JavaScript's idea of white
 * space (`\s`, `trim`) is used instead of Java's. `extractor-fixtures.json` and `docs-goldens.json` hold the outputs of
 * the TypeScript function, and the offline tests compare this port against them.
 */
object Extractor {
    /** DEFAULT_SNIPPET_PREFIXES of the docs. */
    val DEFAULT_PREFIXES: List<String> = listOf("// ", "# ", "/* ", "<!-- ", "<%-- ")

    /** JavaScript `\s`: white space and line terminators. */
    private const val WS = "[\\t\\n\\u000B\\f\\r \\u00a0\\u1680\\u2000-\\u200a\\u2028\\u2029\\u202f\\u205f\\u3000\\ufeff]"

    private fun isJsWhitespace(c: Char): Boolean = Pattern.matches(WS, c.toString())

    private fun trimmedIsEmpty(line: String): Boolean = line.all { isJsWhitespace(it) }

    /** `prefix.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')` */
    private fun escapePrefix(prefix: String): String {
        val special = ".*+?^\${}()|[]\\"
        val out = StringBuilder()
        for (c in prefix) {
            if (c in special) {
                out.append('\\')
            }
            out.append(c)
        }
        return out.toString()
    }

    private fun prefixPattern(prefixes: List<String>): String = prefixes.joinToString("|") { escapePrefix(it) }

    // JS `$` without the m flag matches only at the end of the input, which is `\z` in Java.
    private fun regex(body: String): Pattern = Pattern.compile("^$WS*$body$WS*\\z")

    private fun unindent(lines: List<String>): List<String> {
        if (lines.isEmpty()) {
            return lines
        }
        var minIndent = Int.MAX_VALUE
        for (line in lines) {
            if (!trimmedIsEmpty(line)) {
                val indent = line.indexOfFirst { !isJsWhitespace(it) }
                if (indent != -1 && indent < minIndent) {
                    minIndent = indent
                }
            }
        }
        if (minIndent == Int.MAX_VALUE || minIndent == 0) {
            return lines
        }
        return lines.map {
            if (trimmedIsEmpty(it)) {
                it
            } else {
                it.substring(minIndent)
            }
        }
    }

    /**
     * Port of `extractSnippets(content, snippetIds, prefixInput)`. An empty id list returns the content itself. An empty
     * prefix list falls back to `// `.
     */
    @JvmStatic
    @JvmOverloads
    fun extractSnippets(
        content: String,
        snippetIds: List<String>,
        prefixInput: List<String> = DEFAULT_PREFIXES,
    ): List<String> {
        if (snippetIds.isEmpty()) {
            return listOf(content)
        }
        val prefixes = prefixInput.ifEmpty { listOf("// ") }
        val lines = content.split("\n")
        val result = mutableListOf<String>()

        val combined = prefixPattern(prefixes)
        val anySnippet = regex("($combined)$WS*snippet\\.([a-zA-Z0-9_-]+)")
        val hidePattern = regex("($combined)$WS*snippet\\.hide")
        val showPattern = regex("($combined)$WS*snippet\\.show")
        val blueprintPattern = regex("($combined)$WS*blueprint\\.[a-zA-Z0-9_-]+")

        for (snippetId in snippetIds) {
            for (prefix in prefixes) {
                val escaped = escapePrefix(prefix)
                // The id is not escaped in the original either.
                val startPattern = regex("$escaped$WS*snippet\\.$snippetId")
                val endPattern = regex("$escaped$WS*snippet\\.end")

                var startIndex = -1
                for (i in lines.indices) {
                    if (startPattern.matcher(lines[i]).find()) {
                        startIndex = i + 1
                        break
                    }
                }
                if (startIndex == -1) {
                    continue
                }

                var endIndex = -1
                for (i in startIndex until lines.size) {
                    val line = lines[i]
                    if (endPattern.matcher(line).find()) {
                        endIndex = i
                        break
                    }
                    val match = anySnippet.matcher(line)
                    if (match.find()) {
                        val matchedId = match.group(2)
                        if (matchedId == "hide" || matchedId == "show") {
                            continue
                        }
                        if (matchedId == snippetId) {
                            continue
                        }
                        endIndex = i
                        break
                    }
                }
                if (endIndex == -1) {
                    endIndex = lines.size
                }

                val processed = mutableListOf<String>()
                var hidden = false
                for (line in lines.subList(startIndex, endIndex)) {
                    if (hidePattern.matcher(line).find()) {
                        hidden = true
                        continue
                    }
                    if (showPattern.matcher(line).find()) {
                        hidden = false
                        continue
                    }
                    if (blueprintPattern.matcher(line).find()) {
                        continue
                    }
                    if (!hidden) {
                        processed.add(line)
                    }
                }

                val unindented = unindent(processed)
                if (unindented.isNotEmpty()) {
                    result.addAll(unindented)
                    break
                }
            }
        }
        return result
    }

    /** Port of `findMissingSnippetIds`: the ids that yield no code. */
    @JvmStatic
    @JvmOverloads
    fun findMissingSnippetIds(
        content: String,
        snippetIds: List<String>,
        prefixInput: List<String> = DEFAULT_PREFIXES,
    ): List<String> = snippetIds.filter { extractSnippets(content, listOf(it), prefixInput).isEmpty() }

    /** What a docs page shows for one id: the lines joined by newlines, or null when the id yields no code. */
    @JvmStatic
    @JvmOverloads
    fun region(
        content: String,
        id: String,
        prefixes: List<String> = DEFAULT_PREFIXES,
    ): String? = extractSnippets(content, listOf(id), prefixes).takeIf { it.isNotEmpty() }?.joinToString("\n")
}
