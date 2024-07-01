import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable

plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    id("pubnub.dokka")
    id("pubnub.multiplatform")
    id("pubnub.ios-simulator-test")

}

kotlin {
    js {
        browser {
            testTask {
                useMocha {
                    timeout = "30s"
                }
//                useKarma {
//                    useChrome()
//                }
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":pubnub-core:pubnub-core-api"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(project(":pubnub-core:pubnub-core-impl"))
                implementation(libs.slf4j)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(npm("pubnub", "8.2.4"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(project(":pubnub-kotlin:pubnub-kotlin-test"))
                implementation(kotlin("test"))
                implementation(libs.coroutines.test)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(project(":pubnub-kotlin:pubnub-kotlin-impl"))
            }
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

kotlin {
    cocoapods {
        framework {
            export(project(":pubnub-core:pubnub-core-api"))
        }
    }
}