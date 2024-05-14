plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
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
                api(project(":pubnub-kotlin:pubnub-kotlin-api"))
                api(project(":pubnub-core:pubnub-core-api"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")
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