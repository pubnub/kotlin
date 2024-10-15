pluginManagement {
    includeBuild("build-logic/gradle-plugins")
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}


dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "pubnub"
include("pubnub-kotlin")
include("pubnub-kotlin:pubnub-kotlin-core-api")
include("pubnub-kotlin:pubnub-kotlin-api")
include("pubnub-kotlin:pubnub-kotlin-impl")
include("pubnub-kotlin:pubnub-kotlin-test")
include("pubnub-gson")
include("pubnub-gson:pubnub-gson-api")
include("pubnub-gson:pubnub-gson-impl")
include("examples:kotlin-app")
include("examples:java-app")
includeBuild("build-logic/ktlint-custom-rules")
includeBuild("migration_utils")
