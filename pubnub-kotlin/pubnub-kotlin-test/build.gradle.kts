
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import com.pubnub.gradle.enableIosSimulatorTarget
import com.pubnub.gradle.enableIosTarget
import com.pubnub.gradle.enableJsTarget
import java.util.Properties

plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    id("pubnub.ios-simulator-test")
//    id("pubnub.multiplatform")
    kotlin("multiplatform")

    id("com.codingfeline.buildkonfig") version "0.15.1"
}

kotlin {
    jvmToolchain(8)

    if (enableJsTarget) {
        js {
            useEsModules()
            browser()
            nodejs()
        }
    }
    jvm()
    if (enableIosTarget) {
        iosArm64()
    }
    if (enableIosSimulatorTarget) {
        iosSimulatorArm64()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":pubnub-kotlin:pubnub-kotlin-api"))
                api(project(":pubnub-core:pubnub-core-api"))
                api(kotlin("test"))
                api(libs.coroutines.test)
            }
        }

        val jvmMain by getting {
            dependencies {
                api(kotlin("test-junit"))
            }
        }
    }

    ktlint {
        filter {
            exclude { it: FileTreeElement -> it.file.absolutePath.also { println(it) }.contains("/build/") }
        }
    }

    buildkonfig {
        packageName = "com.pubnub.test"
        exposeObjectWithName = "Keys"

        defaultConfigs {
            val testProps = Properties()
            try {
                rootProject.file("test.properties").inputStream().use {
                    testProps.load(it)
                }
            } catch (e: Exception) {
                println("No test.properties found in root project. Using 'demo' for all keys.")
                testProps.setProperty("pubKey", "demo")
                testProps.setProperty("subKey", "demo")
                testProps.setProperty("pamPubKey", "demo")
                testProps.setProperty("pamSubKey", "demo")
                testProps.setProperty("pamSecKey", "demo")
            }
            buildConfigField(Type.STRING, "pubKey", testProps.getProperty("pubKey"))
            buildConfigField(Type.STRING, "subKey", testProps.getProperty("subKey"))
            buildConfigField(Type.STRING, "pamPubKey", testProps.getProperty("pamPubKey"))
            buildConfigField(Type.STRING, "pamSubKey", testProps.getProperty("pamSubKey"))
            buildConfigField(Type.STRING, "pamSecKey", testProps.getProperty("pamSecKey"))
        }
    }
}
