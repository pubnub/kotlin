plugins {
    alias(libs.plugins.benmanes.versions)
    alias(libs.plugins.lombok)
    id("pubnub.java-library")
    id("pubnub.kotlin-library")
    id("pubnub.dokka")
}

dependencies {
    api(project(":pubnub-kotlin:pubnub-kotlin-api"))
    implementation(libs.slf4j)
    implementation(libs.jetbrains.annotations)
}
