plugins {
    alias(libs.plugins.benmanes.versions)
    alias(libs.plugins.lombok)
    id("pubnub.java-library")
    id("pubnub.dokka")
}

dependencies {
    api(project(":pubnub-gson:pubnub-gson-api"))
    implementation(project(":pubnub-gson:pubnub-gson-impl"))
}
