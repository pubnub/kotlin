plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    id("pubnub.dokka")
    id("pubnub.multiplatform")
    alias(libs.plugins.kotlinx.atomicfu)
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
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
