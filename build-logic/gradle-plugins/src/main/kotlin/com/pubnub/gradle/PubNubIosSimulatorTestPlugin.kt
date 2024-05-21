package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest
import java.io.ByteArrayOutputStream

class PubNubIosSimulatorTestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<KotlinMultiplatformPluginWrapper>()

            val deviceName = project.findProperty("IOS_SIMULATOR_ID") as? String ?: "iPhone 15 Pro"

            tasks.register<Exec>("bootIOSSimulator") {
                isIgnoreExitValue = true
                commandLine("xcrun", "simctl", "boot", deviceName)
                doLast {
                    val result = executionResult.get()
                    if (result.exitValue != 148 && result.exitValue != 149) { // ignoring device already booted errors
                        result.assertNormalExitValue()
                    }
                }
            }

            val shutdownTask = tasks.register<Exec>("shutdownIOSSimulator") {
                commandLine("xcrun", "simctl", "shutdown", deviceName)
            }

            tasks.withType<KotlinNativeSimulatorTest>().configureEach {
                it.dependsOn("bootIOSSimulator")
                it.standalone.set(false)
                it.device.set(deviceName)
                it.finalizedBy(shutdownTask)
            }

            shutdownTask.configure {
                it.dependsOn(tasks.withType<KotlinNativeSimulatorTest>())
            }
        }
    }
}