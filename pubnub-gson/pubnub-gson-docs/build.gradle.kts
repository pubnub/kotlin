plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.kotlin-library")
    id("pubnub.dokka")
    id("pubnub.shared")
}

dependencies {
    api(project(":pubnub-gson:pubnub-gson-api"))
    implementation(project(":pubnub-gson:pubnub-gson-impl"))

    implementation(libs.hamcrest)
}
