import com.pubnub.gradle.enableAnyIosTarget
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
                api(project(":pubnub-kotlin:pubnub-kotlin-core-api"))
                implementation(libs.kotlinx.atomicfu)
            }
        }

        val nonJs = create("nonJs") {
            dependsOn(commonMain)
            dependencies {
                api(libs.kotlinx.datetime)
            }
        }

        val jvmMain by getting {
            dependsOn(nonJs)
            dependencies {
                api(libs.retrofit2)
                api(libs.okhttp)
                api(libs.okhttp.logging)
                api(libs.json)
                api(libs.gson)
                implementation(libs.slf4j)
            }
        }

        if (enableAnyIosTarget) {
            val appleMain by getting {
                dependsOn(nonJs)
                dependencies {
                    api(libs.kotlinx.datetime)
                }
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
