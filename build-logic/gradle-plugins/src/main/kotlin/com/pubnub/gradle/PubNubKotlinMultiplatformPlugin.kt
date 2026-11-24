package com.pubnub.gradle

import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
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
            apply<PubNubBaseKotlinMultiplatformPlugin>()
            // Must be set before vanniktech's afterEvaluate auto-detection, which would pick the Dokka javadoc jar.
            // withType runs now if publishing is already applied, later if it is applied after this plugin,
            // and never for modules without publishing (e.g. *-test).
            plugins.withType<MavenPublishPlugin> {
                extensions.configure<MavenPublishBaseExtension> {
                    // One Dokka jar copied into every KMP publication (10 with all iOS targets) cost about 109 MB
                    // per release. Central only needs a javadoc jar to exist. Hosted KDoc comes from :pubnub-kotlin-docs.
                    configure(KotlinMultiplatform(javadocJar = JavadocJar.Empty(), sourcesJar = true))
                }
            }
            if (enableAnyIosTarget) {
                apply<KotlinCocoapodsPlugin>()
                extensions.configure<KotlinMultiplatformExtension> {
                    (this as? ExtensionAware)?.extensions?.configure<CocoapodsExtension> {
                        ios.deploymentTarget = "14.0"
                        osx.deploymentTarget = "11.0"
                        tvos.deploymentTarget = "14.0"

                        // Maps custom Xcode configuration to NativeBuildType
                        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
                        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE

                        framework {
                            isStatic = true
                            transitiveExport = true
                        }

                        pod("PubNubSwift") {
                            val swiftPath = project.findProperty("SWIFT_PATH") as? String
                            if (swiftPath != null) {
                                source = path(rootProject.file(swiftPath))
                            } else {
                                version = project.rootProject
                                    .extensions
                                    .getByType(VersionCatalogsExtension::class.java)
                                    .named("libs")
                                    .findVersion("pubnub.swift").get().requiredVersion
                            }
                            moduleName = "PubNubSDK"
                            extraOpts += listOf("-compiler-option", "-fmodules")
                        }
                    }
                }
            }
        }
    }
}

class PubNubBaseKotlinMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<KotlinMultiplatformPluginWrapper>()

            // Kotlin
            extensions.configure<KotlinMultiplatformExtension> {
                compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
                jvmToolchain(8)

                if (enableJsTarget) {
                    js { ->
                        project.findProperty("JS_MODULE_NAME")?.toString()?.let { jsModuleName ->
                            outputModuleName.set(jsModuleName)
                        }
                        nodejs {
                            testTask {
                                it.environment("MOCHA_OPTIONS", "--exit")
                                it.useMocha {
                                    timeout = "20s"
                                }
                            }
                        }
                    }
                }

                jvm { ->
                    compilations.configureEach { compilation ->
                        compilation.compileTaskProvider.configure { task ->
                            task.compilerOptions {
                                javaParameters.set(true)
                                freeCompilerArgs.add("-Xjvm-default=all")
                            }
                        }
                    }
                }


                if (enableIosTargetOther) {
                    iosArm64()
                    iosX64()
                    macosArm64()
                    macosX64()
                    tvosArm64()
                    tvosSimulatorArm64()
                }
                if (enableIosSimulatorArm64) {
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
                        getByName("appleMain") {
                            it.dependsOn(nonJvm)
                        }
                    }
                }
            }
            if (enableJsTarget) {
                yarn.yarnLockMismatchReport = YarnLockMismatchReport.WARNING
                yarn.yarnLockAutoReplace = true
            }
            tasks.withType<AbstractTestTask> {
                testLogging {
                    it.showStackTraces = true
                    it.showCauses = true
                    it.showExceptions = true
                    it.exceptionFormat = TestExceptionFormat.FULL
                }
//                if (providers.environmentVariable("CI").isPresent && !providers.environmentVariable("CI_FORCE_RUN_INTEGRATION_TESTS").isPresent) {
//                    filter.excludeTestsMatching("com.pubnub.test.integration.*")
//                }
            }
        }
    }
}

val Project.enableJsTarget get() = project.findProperty("ENABLE_TARGET_JS") == "true"
val Project.enableIosTargetOther get() = project.findProperty("ENABLE_TARGET_IOS_OTHER") == "true" || project.findProperty("ENABLE_TARGET_IOS_ALL") == "true"
val Project.enableIosSimulatorArm64 get() = project.findProperty("ENABLE_TARGET_IOS_SIMULATOR_ARM64") == "true" || project.findProperty("ENABLE_TARGET_IOS_ALL") == "true"
val Project.enableAnyIosTarget get() = enableIosTargetOther || enableIosSimulatorArm64