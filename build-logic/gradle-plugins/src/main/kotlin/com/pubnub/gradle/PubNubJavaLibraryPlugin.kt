package com.pubnub.gradle

import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.SpotBugsExtension
import com.github.spotbugs.snom.SpotBugsPlugin
import com.github.spotbugs.snom.SpotBugsTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.CheckstylePlugin
import org.gradle.api.plugins.quality.Pmd
import org.gradle.api.plugins.quality.PmdExtension
import org.gradle.api.plugins.quality.PmdPlugin
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import java.io.File
import java.util.Properties

class PubNubJavaLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<PubNubSharedPlugin>()
            apply<JavaLibraryPlugin>()
            apply<CheckstylePlugin>()
            apply<PmdPlugin>()
            apply<SpotBugsPlugin>()

            val libs = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")
            val mainSourceSet = extensions.getByType<SourceSetContainer>().getByName("main")

            // Checkstyle 10.x and SpotBugs 4.9+ don't run on the JDK 8 toolchain used for compilation
            val analysisLauncher = extensions.getByType<JavaToolchainService>().launcherFor {
                it.languageVersion.set(JavaLanguageVersion.of(21))
            }

            tasks.withType<JavaCompile>().configureEach {
                it.options.compilerArgs.add("-parameters")
            }

            extensions.configure<JavaPluginExtension> {
                toolchain {
                    it.languageVersion.set(JavaLanguageVersion.of(8))
                }
            }

            // Checkstyle
            extensions.configure<CheckstyleExtension> {
                toolVersion = libs.findVersion("checkstyle").get().requiredVersion
                configFile = rootProject.file("config/checkstyle/checkstyle.xml")
                sourceSets = listOf(mainSourceSet)
            }

            tasks.withType<Checkstyle>().configureEach {
                it.exclude("**/vendor/**", "**/*Test*")
                it.javaLauncher.set(analysisLauncher)

                it.reports { report ->
                    report.xml.required.set(true)
                    report.html.required.set(true)
                }
            }

            // PMD
            extensions.configure<PmdExtension> {
                toolVersion = libs.findVersion("pmd").get().requiredVersion
                // violations within the budget would be printed on every run; see the HTML/XML report instead
                isConsoleOutput = false
                ruleSets = emptyList()
                ruleSetFiles = files(rootProject.file("config/pmd/ruleset.xml"))
                sourceSets = listOf(mainSourceSet)
                // Gradle's PMD plugin has no baseline: fail only when a module exceeds its recorded violation count
                maxFailures.set(pmdMaxFailures(rootProject.file("config/pmd/max-failures.properties"), project.name))
            }

            tasks.withType<Pmd>().configureEach {
                it.exclude("**/vendor/**")

                it.reports { report ->
                    report.xml.required.set(true)
                    report.html.required.set(true)
                }
            }

            // PMD needs the JDK classes on its aux classpath for type resolution; give it the JDK 8 the code targets
            val jdk8RuntimeJar = extensions.getByType<JavaToolchainService>().launcherFor {
                it.languageVersion.set(JavaLanguageVersion.of(8))
            }.map { it.metadata.installationPath.file("jre/lib/rt.jar") }
            tasks.named("pmdMain", Pmd::class.java) {
                it.classpath = (it.classpath ?: files()).plus(files(jdk8RuntimeJar))
            }

            // SpotBugs + FindSecBugs
            extensions.configure<SpotBugsExtension> {
                toolVersion.set(libs.findVersion("spotbugs").get().requiredVersion)
                effort.set(Effort.MAX)
                reportLevel.set(Confidence.MEDIUM)
                excludeFilter.set(rootProject.file("config/spotbugs/exclude.xml"))
                rootProject.file("config/spotbugs/baseline-${project.name}.xml").takeIf { it.exists() }?.let {
                    baselineFile.set(it)
                }
            }

            dependencies {
                "spotbugsPlugins"(libs.findLibrary("findsecbugs").get())
            }

            tasks.withType<SpotBugsTask>().configureEach {
                it.launcher.set(analysisLauncher)
                // main source set only
                it.enabled = it.name == "spotbugsMain"

                it.reports.create("xml") { report ->
                    report.required.set(true)
                }
                it.reports.create("html") { report ->
                    report.required.set(true)
                }
            }

            // Analyse Java bytecode only; Kotlin code is covered by detekt and its bytecode gives many false positives.
            // Kotlin classes stay on the aux classpath so Java -> Kotlin references resolve.
            tasks.named("spotbugsMain", SpotBugsTask::class.java) {
                it.classDirs.setFrom(mainSourceSet.java.classesDirectory)
                it.auxClassPaths.from(mainSourceSet.output.classesDirs)
            }
        }
    }

    private fun pmdMaxFailures(file: File, moduleName: String): Int {
        if (!file.exists()) {
            return 0
        }
        val properties = Properties()
        file.inputStream().use { properties.load(it) }
        return properties.getProperty(moduleName)?.trim()?.toInt() ?: 0
    }
}