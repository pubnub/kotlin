package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

class PubNubKotlinLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<PubNubSharedPlugin>()
            apply<KotlinPluginWrapper>()
            apply<KtlintPlugin>()

            // Kotlin
            extensions.configure<KotlinJvmProjectExtension> {
                jvmToolchain(8)
            }

            tasks.named("compileKotlin", KotlinJvmCompile::class.java) {
                it.compilerOptions {
                    javaParameters.set(true)
                }
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
        }
    }
}