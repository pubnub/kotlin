plugins {
    `java-library`
    jacoco
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.benmanes.versions)
    alias(libs.plugins.maven.publish)
    id("pubnub.shared")
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

tasks.named<Test>("test").configure {
    failFast = true
    exclude("**/contract/*.class")
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

ktlint {
    outputToConsole = true
    verbose = true
    additionalEditorconfig =
        mapOf(
            "ij_kotlin_imports_layout" to "*,java.**,javax.**,kotlin.**,^",
            "indent_size" to "4",
        )
}

dependencies {
    api(project(":pubnub-core:pubnub-core-api"))

    implementation(libs.retrofit2)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(libs.json)
    implementation(libs.gson)

    implementation(libs.slf4j)
    implementation(libs.cbor)

    testImplementation(libs.wiremock)
    testImplementation(libs.logback.classic)
    testImplementation(libs.logback.core)
    testImplementation(libs.cucumber.java)
    testImplementation(libs.cucumber.junit)
    testImplementation(libs.cucumber.picocontainer)
    testImplementation(libs.awaitility)
    testImplementation(libs.junit4)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.junit.vintage.engine)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.owner)

    ktlintRuleset(project(":build-logic:ktlint-custom-rules"))
}

task<Test>("cucumber") {
    systemProperty("cucumber.filter.tags", System.getProperty("cucumber.filter.tags"))
    systemProperty("cucumber.features", System.getProperty("cucumber.features"))
    systemProperty("cucumber.plugins", System.getProperty("cucumber.plugins"))
}
