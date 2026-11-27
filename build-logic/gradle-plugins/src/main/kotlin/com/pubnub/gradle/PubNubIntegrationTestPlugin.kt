package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register

/** Lock shared by the docsSnippetsTest tasks of both SDK modules, so they never run at the same time. */
abstract class DocsSnippetsLock : BuildService<BuildServiceParameters.None>

class PubNubIntegrationTestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<JavaLibraryPlugin>()

            val integrationTestTask = tasks.register<Test>("integrationTest") {
                description = "Runs integration tests."
                group = "verification"

                useJUnitPlatform()
                exclude("com/pubnub/docs/**")
                testLogging {
                    it.events("passed")
                }
            }

            // The Java tests (pubnub-gson) use their own keysets, so the Kotlin and the Java task may run at the same time.
            // Each SDK has its own lock: two runs on the same keysets never overlap.
            val docsSdk = if (path.startsWith(":pubnub-gson")) "java" else "kotlin"
            val docsLock =
                gradle.sharedServices.registerIfAbsent("docsSnippetsLock-$docsSdk", DocsSnippetsLock::class.java) {
                    it.maxParallelUsages.set(1)
                }
            val docsSnippetsTestTask =
                tasks.register<Test>("docsSnippetsTest") {
                    description = "Runs the live docs snippet tests (com.pubnub.docs). Needs the docs keysets, see the tier README."
                    group = "verification"

                    useJUnitPlatform()
                    include("com/pubnub/docs/**")
                    maxParallelForks = 1
                    systemProperty("junit.jupiter.execution.parallel.enabled", "false")
                    // Selects the keysets in Env (com.pubnub.docs.support): plain env names for kotlin, JAVA_ for java.
                    // scripts/dev/docs-snippets.sh sets DOCS_SNIPPETS_TOOL_SDK only for its janitor and preflight commands,
                    // which run in the Kotlin module, so that they can work on the Java keysets too.
                    systemProperty("docs.sdk", providers.environmentVariable("DOCS_SNIPPETS_TOOL_SDK").orNull ?: docsSdk)
                    usesService(docsLock)
                    outputs.upToDateWhen { false }
                    // a class can exist in one SDK module only, so --tests must not fail in the other module
                    filter.isFailOnNoMatchingTests = false
                    testLogging {
                        it.events("passed", "failed", "skipped")
                        it.showStandardStreams = true
                        // Keep Gradle's short exception format (class and line, no message): Gradle prints messages
                        // unredacted. LiveExtension prints each failure message itself, through Redact.
                    }
                    // LIVE_GROUP selects the classes of one group from groups.json (its kotlin and java lists).
                    val liveGroup = providers.environmentVariable("LIVE_GROUP").orNull?.trim().orEmpty()
                    if (liveGroup.isNotEmpty() && liveGroup != "all") {
                        val groupsFile =
                            rootProject.file(
                                "pubnub-kotlin/pubnub-kotlin-impl/src/integrationTest/kotlin/com/pubnub/docs/groups.json",
                            )

                        @Suppress("UNCHECKED_CAST")
                        val groups = (groovy.json.JsonSlurper().parse(groupsFile) as Map<String, Any>)["groups"] as List<Map<String, Any>>
                        val group =
                            groups.firstOrNull { it["name"] == liveGroup }
                                ?: throw org.gradle.api.GradleException("LIVE_GROUP '$liveGroup' is not a group in groups.json")

                        @Suppress("UNCHECKED_CAST")
                        val classes = ((group["kotlin"] as List<String>?).orEmpty() + (group["java"] as List<String>?).orEmpty())
                        // an empty group must select nothing, not everything
                        classes.ifEmpty { listOf("com.pubnub.docs.NoClassInThisGroup") }.forEach { filter.includeTestsMatching(it) }
                    }
                }

            configure<SourceSetContainer> {
                val integrationTestSourceSet = create("integrationTest") {
                    it.compileClasspath += getByName("main").output
                    it.runtimeClasspath += getByName("main").output
                }
                integrationTestTask.configure {
                    it.testClassesDirs = integrationTestSourceSet.output.classesDirs
                    it.classpath = integrationTestSourceSet.runtimeClasspath
                }
                docsSnippetsTestTask.configure {
                    it.testClassesDirs = integrationTestSourceSet.output.classesDirs
                    it.classpath = integrationTestSourceSet.runtimeClasspath
                }
            }

            configurations.getByName("integrationTestImplementation") {
                it.extendsFrom(configurations.getByName("implementation"))
                it.extendsFrom(configurations.getByName("testImplementation"))
            }
        }
    }
}