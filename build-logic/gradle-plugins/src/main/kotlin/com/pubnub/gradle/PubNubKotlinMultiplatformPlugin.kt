package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.KotlinCocoapodsPlugin
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.yarn

class PubNubKotlinMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<KotlinMultiplatformPluginWrapper>()
            if (enableAnyIosTarget) {
                apply<KotlinCocoapodsPlugin>()
            }

            // Kotlin
            extensions.configure<KotlinMultiplatformExtension> {
                compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
                jvmToolchain(8)

                if (enableJsTarget) {
                    js { ->
                        useEsModules()
                        browser {
                            testTask {
                                it.useMocha {
                                    timeout = "10s"
                                }
                            }
                        }
//                    nodejs {
//                        testTask {
//                            it.useMocha {
//                                timeout = "5s"
//                            }
//                        }
//                    }
                    }
                }

                jvm { ->
                    compilations.configureEach { compilation ->
                        compilation.compileTaskProvider.configure { task ->
                            task.compilerOptions {
                                javaParameters.set(true)
                            }
                        }
                    }
                }


                if (enableIosTarget) {
                    iosArm64()
                }
                if (enableIosSimulatorTarget) {
                    iosSimulatorArm64()
                }

                applyDefaultHierarchyTemplate()
                with (sourceSets) {
                    val commonMain = getByName("commonMain")

                    val nonJvm = create("nonJvm") {
                        it.dependsOn(commonMain)
                    }

                    if (enableJsTarget) {
                        getByName("jsMain") {
                            it.dependsOn(nonJvm)
                        }
                    }

                    if (enableAnyIosTarget) {
                        getByName("iosMain") {
                            it.dependsOn(nonJvm)
                        }
                    }
                }

                if (enableAnyIosTarget) {
                    (this as? ExtensionAware)?.extensions?.configure<CocoapodsExtension> {
                        ios.deploymentTarget = "14"
//
//                    summary = "Some description for a Kotlin/Native module"
//                    homepage = "Link to a Kotlin/Native module homepage"

                        // Maps custom Xcode configuration to NativeBuildType
                        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
                        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE

                        framework {
                            isStatic = true
                            transitiveExport = true
                        }

                        pod("PubNubSwift") {
                            val swiftPath = project.findProperty("SWIFT_PATH") as? String ?: "swift"
//                        source = git("https://github.com/pubnub/swift") {
//                            branch = "feat/kmp"
//                        }
//                        version = "7.1.0"
                            source = path(rootProject.file(swiftPath))
                            moduleName = "PubNub"
                            extraOpts += listOf("-compiler-option", "-fmodules")
                        }
                    }
                }
            }
            if (enableJsTarget) {
                yarn.yarnLockMismatchReport = YarnLockMismatchReport.WARNING
                yarn.yarnLockAutoReplace = true
            }
        }
    }
}

val Project.enableJsTarget get() = project.findProperty("ENABLE_TARGET_JS") == "true"
val Project.enableIosTarget get() = project.findProperty("ENABLE_TARGET_IOS") == "true"
val Project.enableIosSimulatorTarget get() = project.findProperty("ENABLE_TARGET_IOS_SIMULATOR") == "true"
val Project.enableAnyIosTarget get() = enableIosTarget || enableIosSimulatorTarget