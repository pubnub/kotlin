import com.pubnub.gradle.enableJsTarget

plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    id("pubnub.dokka")
    id("pubnub.multiplatform")
    id("pubnub.ios-simulator-test")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.atomicfu)
            }
        }

        val jvmMain by getting {
            dependencies {
                api(libs.retrofit2)
                api(libs.okhttp)
                api(libs.okhttp.logging)
                api(libs.json)
                api(libs.gson)
                implementation(libs.slf4j)
                implementation(libs.slf4j)
            }
        }

        if (enableJsTarget) {
            val jsMain by getting {
                dependencies {
                    implementation(npm("pubnub", "8.2.8"))
                }
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
}
