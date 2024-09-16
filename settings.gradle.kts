pluginManagement {
    includeBuild("build-logic/gradle-plugins")
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "pubnub"
include("pubnub-kotlin")
include("pubnub-kotlin:pubnub-kotlin-api")
include("pubnub-kotlin:pubnub-kotlin-impl")
include("pubnub-kotlin:pubnub-kotlin-test")
include("pubnub-gson")
include("pubnub-gson:pubnub-gson-api")
include("pubnub-gson:pubnub-gson-impl")
include("examples:kotlin-app")
include("examples:java-app")
includeBuild("build-logic/ktlint-custom-rules")
