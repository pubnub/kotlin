plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    compileOnly(gradleKotlinDsl())
    compileOnly(libs.nexus.gradlePlugin)
    compileOnly(libs.vanniktech.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ktlint.gradlePlugin)
    compileOnly(libs.dokka.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("pubnubShared") {
            id = "pubnub.shared"
            implementationClass = "com.pubnub.gradle.PubNubSharedPlugin"
        }
        register("pubnubDokka") {
            id = "pubnub.dokka"
            implementationClass = "com.pubnub.gradle.PubNubDokkaPlugin"
        }
        register("pubnubInteg") {
            id = "pubnub.integration-test"
            implementationClass = "com.pubnub.gradle.PubNubIntegrationTestPlugin"
        }
        register("pubnubTest") {
            id = "pubnub.test"
            implementationClass = "com.pubnub.gradle.PubNubTestPlugin"
        }
        register("pubnubJavaLibrary") {
            id = "pubnub.java-library"
            implementationClass = "com.pubnub.gradle.PubNubJavaLibraryPlugin"
        }
        register("pubnubKotlinLibrary") {
            id = "pubnub.kotlin-library"
            implementationClass = "com.pubnub.gradle.PubNubKotlinLibraryPlugin"
        }
        register("pubnubIosSimulatorTest") {
            id = "pubnub.ios-simulator-test"
            implementationClass = "com.pubnub.gradle.PubNubIosSimulatorTestPlugin"
        }
        register("pubnubMultiplatform") {
            id = "pubnub.multiplatform"
            implementationClass = "com.pubnub.gradle.PubNubKotlinMultiplatformPlugin"
        }
        register("pubnubBaseMultiplatform") {
            id = "pubnub.base.multiplatform"
            implementationClass = "com.pubnub.gradle.PubNubBaseKotlinMultiplatformPlugin"
        }
    }
}