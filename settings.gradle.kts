pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "pubnub-kotlin-root"
include("pubnub-kotlin-core")
include("pubnub-kotlin-core:core-api")
include("pubnub-kotlin-core:core-impl")
include("pubnub-kotlin")
include("pubnub-java")
include("examples:kotlin-app")
