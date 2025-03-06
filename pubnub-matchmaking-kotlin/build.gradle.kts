plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.kotlin-library")
    id("pubnub.dokka")
}

dependencies {
    api(project(":pubnub-matchmaking-kotlin:pubnub-matchmaking-kotlin-api"))
    implementation(project(":pubnub-matchmaking-kotlin:pubnub-matchmaking-kotlin-impl"))
}
