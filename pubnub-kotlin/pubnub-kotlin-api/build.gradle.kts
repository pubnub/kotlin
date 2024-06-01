import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    id("pubnub.dokka")
    id("pubnub.multiplatform")
}

kotlin {
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
                implementation(npm("pubnub", "8.1.0"))
            }
        }
    }
}