package com.pubnub.docs.support.tools

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.pubnub.docs.support.Tier
import java.io.File

/**
 * API groups of the live tier, read from `groups.json` (data only). One group is one job of the "Live tests" workflow and
 * one `LIVE_GROUP=<name>` run. `roles` are the keyset roles a run of the group needs. `kotlin` and `java` list the fully
 * qualified names of the test classes of the group in each SDK module, in the order of the serial run.
 *
 * [check] fails on a test class in no group or in two groups, a listed class that does not exist, a role that a class uses
 * but its group does not list, an unknown role, and a workflow `group` input whose options differ from the group names.
 */
object Groups {
    class Group(val name: String, val roles: List<String>, val kotlin: List<String>, val java: List<String>) {
        fun classes(docset: String): List<String> {
            if (docset == "kotlin") {
                return kotlin
            }
            return java
        }
    }

    private val knownRoles = setOf("open", "pam", "datasync")
    private val workflowFile get() = File(Tier.repoRoot, ".github/workflows/live-integration.yml")

    private fun strings(
        group: JsonObject,
        key: String,
    ): List<String> = group.getAsJsonArray(key)?.map { it.asString } ?: emptyList()

    @JvmStatic
    fun load(file: File = Tier.file("groups.json")): List<Group> {
        val root = JsonParser.parseString(file.readText()).asJsonObject
        return root.getAsJsonArray("groups").map { element ->
            val group = element.asJsonObject
            Group(group.get("name").asString, strings(group, "roles"), strings(group, "kotlin"), strings(group, "java"))
        }
    }

    @JvmStatic
    fun groupOfClass(className: String): Group? = load().firstOrNull { className in it.kotlin || className in it.java }

    private fun packageOf(file: SnippetScan.ScannedFile): String {
        val line = file.lines.firstOrNull { it.trim().startsWith("package ") } ?: return ""
        return line.trim().removePrefix("package ").removeSuffix(";").trim()
    }

    private fun hasTests(file: SnippetScan.ScannedFile): Boolean {
        return file.lines.any { it.trim().startsWith("@Test") || it.trim().startsWith("@ParameterizedTest") }
    }

    /** The docs test classes found in a scanned tree, keyed by `<docset>:<fully qualified name>`. */
    fun testClasses(files: List<SnippetScan.ScannedFile>): Map<String, SnippetScan.ScannedFile> {
        return files.filter { it.kind == "test" && hasTests(it) }
            .associateBy { it.docset + ":" + packageOf(it) + "." + it.path.nameWithoutExtension }
    }

    private fun checkGroupLists(
        groups: List<Group>,
        classes: Map<String, SnippetScan.ScannedFile>,
        problems: MutableList<String>,
    ) {
        for (group in groups) {
            for (docset in listOf("kotlin", "java")) {
                val names = group.classes(docset)
                names.filter { "$docset:$it" !in classes }.forEach {
                    problems.add("groups.json: group ${group.name} lists $it ($docset), which is not a test class of the tier")
                }
                names.groupBy { it }.filterValues { it.size > 1 }.keys.forEach {
                    problems.add("groups.json: group ${group.name} lists $it ($docset) twice")
                }
            }
        }
    }

    /** Returns the problems. [files] defaults to a scan of both trees. */
    @JvmStatic
    @JvmOverloads
    fun check(
        groups: List<Group> = load(),
        files: List<SnippetScan.ScannedFile> = SnippetScan.scan(),
        workflow: File? = workflowFile,
    ): List<String> {
        val problems = mutableListOf<String>()
        val twice = groups.groupBy { it.name }.filterValues { it.size > 1 }.keys
        twice.forEach { problems.add("groups.json: group name \"$it\" is used twice") }
        for (group in groups) {
            val unknown = group.roles.filter { it !in knownRoles }
            unknown.forEach { problems.add("groups.json: group ${group.name} lists the unknown role \"$it\"") }
        }
        val classes = testClasses(files)
        for ((key, file) in classes) {
            val docset = key.substringBefore(":")
            val name = key.substringAfter(":")
            val owners = groups.filter { name in it.classes(docset) }
            if (owners.isEmpty()) {
                problems.add("groups.json: test class $name ($docset) is in no group")
            } else if (owners.size > 1) {
                problems.add("groups.json: test class $name ($docset) is in ${owners.size} groups: ${owners.joinToString { it.name }}")
            } else {
                val missing = SnippetScan.rolesIn(file.text).filter { it !in owners[0].roles }
                if (missing.isNotEmpty()) {
                    val roles = missing.joinToString()
                    problems.add("groups.json: $name uses the role(s) $roles but group ${owners[0].name} does not list them")
                }
            }
        }
        checkGroupLists(groups, classes, problems)
        if (workflow != null && workflow.isFile) {
            val options = workflowOptions(workflow)
            val names = groups.map { it.name }.toSet()
            if (options != names) {
                problems.add("${workflow.name}: the group input options ${options.sorted()} differ from the group names ${names.sorted()}")
            }
        }
        return problems
    }

    /** The options of the `group` input of a workflow, without "all". Read with a simple line scan. */
    private fun workflowOptions(workflow: File): Set<String> {
        val lines = workflow.readLines()
        val start = lines.indexOfFirst { Regex("""^\s*group:\s*$""").matches(it) }
        if (start < 0) {
            return emptySet()
        }
        val options = lines.drop(start).dropWhile { !Regex("""^\s*options:\s*$""").matches(it) }.drop(1)
        return options.takeWhile { Regex("""^\s*-\s+\S+\s*$""").matches(it) }
            .map { it.trim().removePrefix("-").trim() }
            .filter { it != "all" }
            .toSet()
    }

    /** The job matrix of the workflow: `{"include":[{"group":"<name>"}, ...]}`. */
    @JvmStatic
    fun matrix(groups: List<Group> = load()): String {
        val include = groups.map { mapOf("group" to it.name) }
        return GsonBuilder().create().toJson(mapOf("include" to include))
    }
}
