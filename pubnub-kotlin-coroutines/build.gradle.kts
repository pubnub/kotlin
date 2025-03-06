plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.kotlin-library")
    id("pubnub.dokka")
    id("pubnub.shared")
}

dependencies {
    api(project(":pubnub-kotlin:pubnub-kotlin-api"))
    api(libs.coroutines)
    implementation(kotlin("test"))
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter)
}
