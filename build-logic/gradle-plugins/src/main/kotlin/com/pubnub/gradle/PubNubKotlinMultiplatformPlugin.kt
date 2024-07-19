package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.DokkaDefaults.moduleName
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.KotlinCocoapodsPlugin
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable

class PubNubKotlinMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<KotlinMultiplatformPluginWrapper>()
            apply<KotlinCocoapodsPlugin>()

            // Kotlin
            extensions.configure<KotlinMultiplatformExtension> {
                jvmToolchain(8)

                js {
                    browser {
                    }
                    binaries.executable()
                }
                jvm {
                    compilations.all {
                        it.compileTaskProvider.configure {
                            it.compilerOptions {
                                freeCompilerArgs.add("-Xexpect-actual-classes")
                                javaParameters.set(true)
                            }
                        }
                    }
                }

                listOf(
                    iosArm64(),
                    // iosX64(),
                    iosSimulatorArm64(),
                ).forEach {
                    it.binaries {
                        framework {
                            isStatic = true
                        }
                    }
                }

                (this as? ExtensionAware)?.extensions?.configure<CocoapodsExtension> {
                    ios.deploymentTarget = "14"

                    // Required properties
                    // Specify the required Pod version here. Otherwise, the Gradle project version is used.
                    version = "1.0"
                    summary = "Some description for a Kotlin/Native module"
                    homepage = "Link to a Kotlin/Native module homepage"

                    // Maps custom Xcode configuration to NativeBuildType
                    xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
                    xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE

//        podfile = project.file(project.file("Sample Chat app/Podfile"))

                    framework {
                        // Required properties
                        // Framework name configuration. Use this property instead of deprecated 'frameworkName'

                        // Optional properties
                        // Specify the framework linking type. It's dynamic by default.
                        isStatic = true
                        transitiveExport = true
                    }

                    pod("PubNubSwift") {
//                        source = git("https://github.com/pubnub/swift") {
//                            branch = "feat/kmp"
//                        }
//            headers = "PubNub/PubNub.h"
                        source = path(rootProject.file("swift"))
//            version = "7.1.0"

                        moduleName = "PubNub"
                        extraOpts += listOf("-compiler-option", "-fmodules")
                    }
                }
            }

        }
    }
}