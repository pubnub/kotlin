plugins {
    checkstyle
    alias(libs.plugins.benmanes.versions)
    alias(libs.plugins.lombok)
    id("pubnub.shared")
    id("pubnub.dokka")
    id("pubnub.integration-test")
}

dependencies {
    api(project(":pubnub-core:pubnub-core-api"))
    implementation(project(":pubnub-core:pubnub-core-impl"))

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

    "integrationTestImplementation"(libs.owner)
}

checkstyle {
    toolVersion = "8.14"
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
    sourceSets = listOf(project.sourceSets.getByName("main"))
}

tasks.withType<Checkstyle>().configureEach {
    exclude("**/vendor/**", "**/*Test*")

    reports {
        xml.required = true
        html.required = true
    }
}
