import com.pubnub.gradle.enableAnyIosTarget
import com.pubnub.gradle.enableJsTarget
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

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
                api(project(":pubnub-core:pubnub-core-api"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(project(":pubnub-core:pubnub-core-impl"))
                implementation(libs.slf4j)
            }
        }

        if (enableJsTarget) {
            val jsMain by getting {
                dependencies {
                    implementation(npm("pubnub", "8.2.4"))
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
    if (enableAnyIosTarget) {
        (this as ExtensionAware).extensions.configure<CocoapodsExtension> {
            framework {
                export(project(":pubnub-core:pubnub-core-api"))
            }
        }
    }
}
