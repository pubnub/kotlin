plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    id("pubnub.dokka")
    kotlin("multiplatform")
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
    }
}