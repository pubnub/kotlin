plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    id("pubnub.integration-test")
}

dependencies {
    api(project(":pubnub-core:pubnub-core-api"))
    api(project(":pubnub-kotlin:pubnub-kotlin-api"))
    implementation(project(":pubnub-core:pubnub-core-impl"))
    implementation(libs.slf4j)

    testImplementation(libs.wiremock)

    testImplementation(libs.logback.classic)
    testImplementation(libs.logback.core)
    testImplementation(libs.json)

    testImplementation(libs.junit4)
    testImplementation(libs.awaitility)
    testImplementation(libs.junit.vintage.engine)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.owner)
}
