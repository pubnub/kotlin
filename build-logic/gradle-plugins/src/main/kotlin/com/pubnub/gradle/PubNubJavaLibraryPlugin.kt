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
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class PubNubJavaLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<PubNubSharedPlugin>()
            apply<JavaLibraryPlugin>()
            apply<CheckstylePlugin>()
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

            // SpotBugs + FindSecBugs
            extensions.configure<SpotBugsExtension> {
                toolVersion.set(libs.findVersion("spotbugs").get().requiredVersion)
                effort.set(Effort.MAX)
                reportLevel.set(Confidence.MEDIUM)
                excludeFilter.set(rootProject.file("config/spotbugs/exclude.xml"))
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
            val spotbugsMain = tasks.named("spotbugsMain", SpotBugsTask::class.java) {
                it.classDirs.setFrom(mainSourceSet.java.classesDirectory)
                it.auxClassPaths.from(mainSourceSet.output.classesDirs)
            }

            // On-demand SpotBugs baseline update (not part of `check`/CI): when `spotbugsBaseline` is requested,
            // spotbugsMain reports every bug (old + new) without failing, and spotbugsBaseline stores that report
            val spotbugsBaselineFile = rootProject.file("config/spotbugs/baseline-${project.name}.xml")
            // checked on the task graph, so any way of requesting the task (camel-case abbreviation, path) is detected
            gradle.taskGraph.whenReady { graph ->
                val updatingSpotbugsBaseline = graph.hasTask("${project.path}:spotbugsBaseline")
                spotbugsMain.configure {
                    if (updatingSpotbugsBaseline) {
                        it.ignoreFailures = true
                    } else if (spotbugsBaselineFile.exists()) {
                        it.baselineFile.set(spotbugsBaselineFile)
                    }
                }
            }
            tasks.register("spotbugsBaseline") { task ->
                task.group = "verification"
                task.description = "Writes all current SpotBugs findings of the main source set to config/spotbugs/baseline-<module>.xml"
                task.dependsOn(spotbugsMain)
                task.doLast {
                    val report = spotbugsMain.get().reports.getByName("xml").outputLocation.get().asFile
                    // modules without Java sources (e.g. the :pubnub-gson aggregator) skip spotbugsMain, so no report
                    if (!report.exists()) {
                        task.logger.lifecycle("No SpotBugs report for ${project.path}, baseline not written")
                        return@doLast
                    }
                    // drop the <Project> element's content: it only lists local absolute paths
                    val baseline = report.readText().replace(
                        Regex("""(<Project projectName="[^"]*")>.*?</Project>""", RegexOption.DOT_MATCHES_ALL),
                        "$1/>"
                    )
                    spotbugsBaselineFile.writeText(stripVolatileReportAttributes(baseline))
                    task.logger.lifecycle("SpotBugs baseline written to $spotbugsBaselineFile")
                }
            }
        }
    }

    // Timestamps and run statistics change on every analysis; pin them so the baseline only changes when findings do.
    // The attributes are kept (with fixed values) because SpotBugs requires some of them when reading the baseline.
    private fun stripVolatileReportAttributes(report: String): String {
        return report
            .replace(Regex("""\b(timestamp|analysisTimestamp)="\d+""""), "$1=\"0\"")
            .replace(Regex("""\btimestamp="[A-Z][a-z]{2}, [^"]*""""), "timestamp=\"Thu, 1 Jan 1970 00:00:00 +0000\"")
            .replace(Regex("""\b(cpu_seconds|clock_seconds|peak_mbytes|alloc_mbytes|gc_seconds)="[^"]*""""), "$1=\"0.00\"")
    }
}