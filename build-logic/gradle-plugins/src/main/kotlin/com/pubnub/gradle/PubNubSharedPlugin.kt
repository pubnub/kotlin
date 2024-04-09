package com.pubnub.gradle

import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

class PubNubSharedPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<MavenPublishPlugin>()
            apply<KotlinPluginWrapper>()
            apply<JavaLibraryPlugin>()
            apply<JacocoPlugin>()
            apply<KtlintPlugin>()

            group = providers.gradleProperty("GROUP").get()
            version = providers.gradleProperty("VERSION_NAME").get()

            // Kotlin
            extensions.configure<KotlinJvmProjectExtension> {
                jvmToolchain(8)
            }

            // Ktlint
            extensions.configure<KtlintExtension> {
                outputToConsole.set(true)
                verbose.set(true)
                additionalEditorconfig.set(
                    mapOf(
                        "ij_kotlin_imports_layout" to "*,java.**,javax.**,kotlin.**,^",
                        "indent_size" to "4",
                        "ktlint_standard_multiline-expression-wrapping" to "disabled",
                        "ktlint_standard_string-template-indent" to "disabled",
                        "ktlint_standard_max-line-length" to "disabled",
                        "ktlint_standard_if-else-wrapping" to "disabled",
                        "ktlint_standard_discouraged-comment-location" to "disabled",
                        "ktlint_standard_trailing-comma-on-declaration-site" to "disabled",
                        "ktlint_standard_trailing-comma-on-call-site" to "disabled",
                        "ktlint_standard_function-signature" to "disabled",
                    )
                )
            }

            dependencies {
                "ktlintRuleset"(project(":build-logic:ktlint-custom-rules"))
            }

            // Tests
            tasks.named<Test>("test").configure { test ->
                test.failFast = true
                test.exclude("**/contract/*.class")
                test.useJUnitPlatform()
            }

            tasks.withType<JacocoReport>().configureEach { task ->
                task.reports {
                    it.xml.required.set(true)
                    it.html.required.set(true)
                }
            }
        }
    }
}