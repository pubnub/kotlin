import org.jetbrains.dokka.DokkaDefaults.moduleName
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    id("pubnub.dokka")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}

kotlin {
    jvmToolchain(8)
    js {
        browser {
        }
        binaries.executable()
    }
    jvm {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                    javaParameters.set(true)
                }
            }
        }
    }

    listOf(
        iosArm64(),
//        iosX64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries {
            framework {
                baseName = "PubNubKMP"
                isStatic = true
            }
        }
    }

    cocoapods {
        ios.deploymentTarget = "14"

        // Required properties
        // Specify the required Pod version here. Otherwise, the Gradle project version is used.
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"

        // Optional properties
        // Configure the Pod name here instead of changing the Gradle project name
        name = "PubNubKMP"

        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE

//        podfile = project.file(project.file("Sample Chat app/Podfile"))

        framework {
            // Required properties
            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
            baseName = "PubNubKMP"

            // Optional properties
            // Specify the framework linking type. It's dynamic by default.
            isStatic = true
            export(project(":pubnub-core:pubnub-core-api"))
            transitiveExport = true
        }

        pod("PubNubSwift") {
            source = git("https://github.com/pubnub/swift") {
                branch = "feat/kmp"
            }
//            headers = "PubNub/PubNub.h"
//            source = path(project.file("swift"))
//            version = "7.1.0"

            moduleName = "PubNub"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":pubnub-core:pubnub-core-api"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(project(":pubnub-core:pubnub-core-impl"))
                implementation(libs.slf4j)
            }
        }
    }
}