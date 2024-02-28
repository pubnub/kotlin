pluginManagement {
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
// include("pubnub-core")
include("pubnub-core:pubnub-core-api")
include("pubnub-core:pubnub-core-impl")
include("pubnub-kotlin")
include("pubnub-gson")
include("examples:kotlin-app")
include("examples:java-app")
