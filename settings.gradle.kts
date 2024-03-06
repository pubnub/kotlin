pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "pubnub"
// include("pubnub-core")
include("pubnub-core:pubnub-core-api")
include("pubnub-core:pubnub-core-impl")
include("pubnub-kotlin")
include("pubnub-gson")
include("examples:kotlin-app")
include("examples:java-app")
include("build-logic:ktlint-custom-rules")
