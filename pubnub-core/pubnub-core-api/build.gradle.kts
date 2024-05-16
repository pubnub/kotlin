plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    id("pubnub.dokka")
    kotlin("multiplatform")
}

kotlin {
    js() {
        browser {
        }
        binaries.executable()
    }
    jvmToolchain(8)
    jvm {
        compilations.all {
            compilerOptions.configure {
                javaParameters.set(true)
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
