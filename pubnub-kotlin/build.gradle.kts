plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.kotlin-library")
    id("pubnub.dokka")
}

dependencies {
    api(project(":pubnub-kotlin:pubnub-kotlin-api"))
    implementation(project(":pubnub-kotlin:pubnub-kotlin-impl"))
}
