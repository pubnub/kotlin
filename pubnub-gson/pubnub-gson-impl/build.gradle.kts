plugins {
    alias(libs.plugins.benmanes.versions)
    alias(libs.plugins.lombok)
    id("pubnub.java-library")
    id("pubnub.kotlin-library")
    id("pubnub.test")
    id("pubnub.dokka")
    id("pubnub.integration-test")
}

dependencies {
    api(project(":pubnub-gson:pubnub-gson-api"))
    implementation(project(":pubnub-kotlin:pubnub-kotlin-impl"))

    implementation(libs.slf4j)
    implementation(libs.jetbrains.annotations)

    testImplementation(libs.logback.classic)
    testImplementation(libs.logback.core)
    testImplementation(libs.hamcrest)
    testImplementation(libs.junit4)
    testImplementation(libs.wiremock)
    testImplementation(libs.awaitility)
    testImplementation(libs.mockito)
    testImplementation(libs.owner)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.vintage.engine)
    testImplementation(libs.awaitility)
    testImplementation(libs.mockk)
    testImplementation("com.google.guava:guava:33.3.0-jre")
    "integrationTestImplementation"(libs.owner)
}
