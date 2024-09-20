package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest
import java.io.ByteArrayOutputStream

class PubNubIosSimulatorTestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<KotlinMultiplatformPluginWrapper>()

            val deviceName = project.findProperty("IOS_SIMULATOR_ID") as? String ?: "iPhone 15 Pro"
            val isMacOs = providers.systemProperty("os.name").map { it.contains("mac", ignoreCase = true) }

            val bootTask = tasks.register<Exec>("bootIOSSimulator") {
                onlyIf { isMacOs.get() }
                isIgnoreExitValue = true
                commandLine("xcrun", "simctl", "boot", deviceName)
                doLast {
                    val result = executionResult.get()
                    if (result.exitValue != 148 && result.exitValue != 149) { // ignoring device already booted errors
                        result.assertNormalExitValue()
                    }
                }
            }

//            val shutdownTask = tasks.register<Exec>("shutdownIOSSimulator") {
//                onlyIf { isMacOs.get() }
//                commandLine("xcrun", "simctl", "shutdown", deviceName)
//                dependsOn(tasks.withType<KotlinNativeSimulatorTest>())
//            }

            tasks.withType<KotlinNativeSimulatorTest>().configureEach {
                it.onlyIf { isMacOs.get() }
                it.standalone.set(false)
                it.device.set(deviceName)
                it.dependsOn(bootTask)
//                it.finalizedBy(shutdownTask)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                targets.withType<KotlinNativeTarget> {
                    if (konanTarget.family.isAppleFamily) {
                        binaries.withType<TestExecutable> {
                            freeCompilerArgs += listOf("-e", "testlauncher.mainBackground")
                        }
                    }
                }
            }
        }
    }
}