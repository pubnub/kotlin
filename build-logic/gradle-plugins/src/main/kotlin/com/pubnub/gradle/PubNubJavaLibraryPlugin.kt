package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.CheckstylePlugin
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class PubNubJavaLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<PubNubSharedPlugin>()
            apply<JavaLibraryPlugin>()
            apply<CheckstylePlugin>()

            tasks.withType<JavaCompile>().configureEach {
                it.options.compilerArgs.add("-parameters")
            }

            extensions.configure<JavaPluginExtension> {
                toolchain {
                    it.languageVersion.set(JavaLanguageVersion.of(8))
                }
            }

            extensions.configure<CheckstyleExtension> {
                toolVersion = "8.14"
                configFile = rootProject.file("config/checkstyle/checkstyle.xml")
                sourceSets = listOf(extensions.getByType<SourceSetContainer>().getByName("main"))
            }

            tasks.withType<Checkstyle>().configureEach {
                it.exclude("**/vendor/**", "**/*Test*")

                it.reports { report ->
                    report.xml.required.set(true)
                    report.html.required.set(true)
                }
            }

        }
    }
}