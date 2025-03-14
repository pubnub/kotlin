@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.pubnub.gradle.enableAnyIosTarget
import com.pubnub.gradle.enableJsTarget
import com.pubnub.gradle.tasks.GenerateVersionTask
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

plugins {
    alias(libs.plugins.kotlinx.atomicfu) // todo do we need lib for atomic operations
    id("pubnub.ios-simulator-test") // start x-code ios simulator before ios test run
    id("pubnub.shared")
    id("pubnub.dokka")
    id("pubnub.multiplatform")
//    alias(libs.plugins.mokkery) // todo downgrade version in libs.version.toml to be compatible with used kotlin version(2.0.21)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.coroutines.core) // todo this is needed for Service that simulates matchmaking REST api call
                implementation(project(":pubnub-matchmaking-kotlin:pubnub-matchmaking-kotlin-api"))
                implementation(project(":pubnub-kotlin:pubnub-kotlin-api"))
                implementation(libs.kotlinx.atomicfu) // todo this is needed for Service that simulates matchmaking REST api call
                implementation(libs.touchlab.kermit)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
//                implementation(project(":pubnub-matchmaking-test"))
            }
        }

        val jvmMain by getting {
            dependencies {
//                implementation(project(":pubnub-kotlin")) //todo it shouldn't be required but because of some error might be
                implementation(kotlin("test-junit"))
            }
        }

        if (enableJsTarget) {
            val jsTest by getting {
                dependencies {
                    implementation(kotlin("test-js")) // to jest potrzebne bo testy KMP na targecie JS nie działały, mimo, że powinny
                }
            }
        }
    }

    if (enableAnyIosTarget) {
        (this as ExtensionAware).extensions.configure<CocoapodsExtension> {
            summary = "Some description for a Kotlin/Native module"
            homepage = "Link to a Kotlin/Native module homepage"
        }
    }
}

val generateVersion =
    tasks.register<GenerateVersionTask>("generateVersion") {
        fileName.set("MatchmakingVersion")
        packageName.set("com.pubnub.matchmaking.internal")
        constName.set("PUBNUB_MATCHMAKING_VERSION")
        version.set(providers.gradleProperty("VERSION_NAME"))
        outputDirectory.set(
            layout.buildDirectory.map {
                it.dir("generated/sources/generateVersion")
            },
        )
    }

kotlin.sourceSets.getByName("commonMain").kotlin.srcDir(generateVersion)
