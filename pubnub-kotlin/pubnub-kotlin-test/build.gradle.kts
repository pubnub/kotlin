import com.codingfeline.buildkonfig.compiler.FieldSpec.Type
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable
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

    js {
        browser {
        }
        binaries.executable()
    }
    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
//        val commonMain by getting {
//            dependencies {
//                api(project(":pubnub-kotlin:pubnub-kotlin-api"))
//                api(project(":pubnub-core:pubnub-core-api"))
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
//                implementation(libs.datetime)
//            }
//        }
//
//        val commonTest by getting {
//            dependencies {
//                implementation(kotlin("test"))
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
//                implementation(libs.coroutines.test)
//            }
//        }
//
//        val jvmMain by getting {
//            dependencies {
//                implementation(project(":pubnub-core:pubnub-core-impl"))
//            }
//        }
//
//        val jvmTest by getting {
//            dependencies {
//                implementation(project(":pubnub-kotlin:pubnub-kotlin-impl"))
//            }
//        }
    }


//    targets.withType<KotlinNativeTarget> {
//        if (konanTarget.family.isAppleFamily) {
//            binaries.withType<TestExecutable> {
//                freeCompilerArgs += listOf("-e", "testlauncher.mainBackground")
//            }
//        }
//    }

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