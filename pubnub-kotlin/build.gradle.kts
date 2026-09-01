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
                api(project(":pubnub-kotlin:pubnub-kotlin-api"))
            }
        }

        val nonJs = create("nonJs") {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependsOn(nonJs)
            dependencies {
                implementation(project(":pubnub-kotlin:pubnub-kotlin-impl"))
            }
        }

        if (project.enableAnyIosTarget) {
            val appleMain by getting {
                dependsOn(nonJs)
            }
        }

        if (project.enableJsTarget) {
            val jsMain by getting {
                // JS-specific dependencies if needed
            }
        }
    }
}
