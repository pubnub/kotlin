plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    id("pubnub.dokka")
}

dependencies {
    api(project(":pubnub-core:pubnub-core-api"))
    implementation(project(":pubnub-core:pubnub-core-impl"))
    implementation(libs.slf4j)
}
