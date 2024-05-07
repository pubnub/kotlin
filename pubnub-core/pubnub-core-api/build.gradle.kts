plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    id("pubnub.dokka")
    kotlin("multiplatform")
}

kotlin {
    js(IR) {
        browser {
        }
        binaries.executable()
    }
    jvm {
        compilations.all {
            jvmToolchain(8)
            compilerOptions.configure {
                javaParameters.set(true)
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {

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
    }
}
