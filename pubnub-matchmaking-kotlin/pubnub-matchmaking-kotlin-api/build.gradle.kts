import com.pubnub.gradle.enableAnyIosTarget // why ENABLE_TARGET_IOS_OTHER=false
import com.pubnub.gradle.enableJsTarget

plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    id("pubnub.dokka")
    id("pubnub.multiplatform") // adds plugin to enables KMP
    id("pubnub.ios-simulator-test") // todo what is this -> do odpalania testów na symulatorze ios
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":pubnub-kotlin:pubnub-kotlin-api"))
                // todo do sprawdzania na projekcie z niższym kotlinem wymuszając niższa wersję odpowiednią komenda
                implementation(libs.kotlinx.atomicfu) // todo in kotlin 2.1.2 this will be in standard library
            }
        }
        val jvmMain by getting {
            dependencies {
                api(libs.retrofit2)
                api(libs.okhttp)
                api(libs.okhttp.logging)
                api(libs.gson)
                implementation(libs.slf4j)
            }
        }
        if (enableAnyIosTarget) {
            val appleMain by getting {
                dependencies {
                }
            }
        }

        if (enableJsTarget) {
            val jsMain by getting {
                dependencies {
                }
            }
        }

        val commonTest by getting {
            dependencies {
//                implementation(project(":pubnub-kotlin:pubnub-kotlin-test")) // todo not needed for now
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}
