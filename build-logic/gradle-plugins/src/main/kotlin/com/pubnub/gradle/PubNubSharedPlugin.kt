package com.pubnub.gradle

import com.vanniktech.maven.publish.MavenPublishPlugin
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.gitlab.arturbosch.detekt.getSupportedKotlinVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.withType
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

class PubNubSharedPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            if (!target.name.endsWith("-test")) {
                apply<MavenPublishPlugin>()
                extensions.configure<PublishingExtension> {
                    repositories {
                        it.maven(uri(rootProject.layout.buildDirectory.dir("repo"))) { ->
                            name = "repo"
                        }
                    }
                }
            }
            apply<KtlintPlugin>()

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

            // Detekt (docs modules hold documentation snippets only)
            if (!target.name.endsWith("-docs")) {
                configureDetekt()
            }
        }
    }

    private fun Project.configureDetekt() {
        apply<DetektPlugin>()

        extensions.configure<DetektExtension> {
            config.setFrom(rootProject.file("config/detekt/detekt.yml"))
            buildUponDefaultConfig = true
            baseline = rootProject.file("config/detekt/baseline-${project.name}.xml")
            parallel = true
            // main (non-test) source sets only: src/main, src/commonMain, src/jvmMain, src/nonJvm, ...
            source.setFrom(
                provider {
                    file("src").listFiles { dir -> dir.isDirectory && !dir.name.endsWith("Test") && dir.name != "test" }
                        .orEmpty()
                        .flatMap { listOf(it.resolve("kotlin"), it.resolve("java")) }
                }
            )
        }

        // detekt 1.23.x embeds its own Kotlin compiler; keep it from being aligned to the project's Kotlin version
        configurations.matching { it.name == "detekt" }.configureEach {
            it.resolutionStrategy.eachDependency { details ->
                if (details.requested.group == "org.jetbrains.kotlin") {
                    details.useVersion(getSupportedKotlinVersion())
                }
            }
        }

        tasks.withType<Detekt>().configureEach {
            it.jvmTarget = "1.8"
            it.reports { report ->
                report.xml.required.set(true)
                report.html.required.set(true)
                report.sarif.required.set(true)
                report.txt.required.set(false)
                report.md.required.set(false)
            }
        }
        tasks.withType<DetektCreateBaselineTask>().configureEach {
            it.jvmTarget = "1.8"
        }
    }
}