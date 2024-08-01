package com.pubnub.gradle

import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.repositories
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

class PubNubSharedPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<MavenPublishPlugin>()
            apply<KtlintPlugin>()

            extensions.configure<PublishingExtension> {
                repositories {
                    it.maven(uri(rootProject.layout.buildDirectory.dir("repo"))) { ->
                        name = "repo"
                    }
                }
            }

            group = providers.gradleProperty("GROUP").get()
            version = providers.gradleProperty("VERSION_NAME").get()

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
                        "ktlint_standard_filename" to "disabled",
                        "ktlint_standard_package-name" to "disabled",
                    )
                )
            }

            dependencies {
                "ktlintRuleset"("com.pubnub:ktlint-custom-rules:1.0.0")
            }
        }
    }
}