import org.jetbrains.dokka.DokkaDefaults.moduleName
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable

plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
}

kotlin {
    jvmToolchain(8)
    js {
        browser {
            testTask {
                useMocha {
                    timeout = "10s"
                }
            }

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
    )

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":pubnub-kotlin:pubnub-kotlin-api"))
                api(project(":pubnub-core:pubnub-core-api"))
                implementation(libs.datetime)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
                implementation(libs.coroutines.test)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(project(":pubnub-kotlin:pubnub-kotlin-impl"))
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
        name = "PubNubKMPTest"

        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE

        framework {
            // Required properties
            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
            baseName = "PubNubKMPTest"

            // Optional properties
            // Specify the framework linking type. It's dynamic by default.
            isStatic = true

            export(":pubnub-kotlin:pubnub-kotlin-api")
            export(":pubnub-core:pubnub-core-api")
            transitiveExport = true
        }

        pod("PubNubSwift") {
            source = git("https://github.com/pubnub/swift") {
                branch = "feat/kmp"
            }

            moduleName = "PubNub"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    targets.withType<KotlinNativeTarget> {
        if (konanTarget.family.isAppleFamily) {
            binaries.withType<TestExecutable> {
                freeCompilerArgs += listOf("-e", "testlauncher.mainBackground")
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest>().configureEach {
    standalone.set(false)
    device.set("iPhone 15 Pro")
}
