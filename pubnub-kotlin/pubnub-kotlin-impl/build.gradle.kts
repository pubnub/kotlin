import com.pubnub.gradle.tasks.GenerateVersionTask

plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.java-library")
    id("pubnub.kotlin-library")
    id("pubnub.test")
    id("pubnub.integration-test")
}

val generateVersion =
    tasks.register<GenerateVersionTask>("generateVersion") {
        version.set(providers.gradleProperty("VERSION_NAME"))
        outputDirectory.set(
            layout.buildDirectory.map {
                it.dir("generated/sources/generateVersion")
            },
        )
    }

kotlin.sourceSets.getByName("main").kotlin.srcDir(generateVersion)

dependencies {
    api(project(":pubnub-kotlin:pubnub-kotlin-api"))
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
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(libs.json)
    implementation(libs.gson)

    implementation(libs.slf4j)
    implementation(libs.cbor)

    testImplementation(libs.wiremock)
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
}

task<Test>("cucumber") {
    filter {
        // include all tests from package
        includeTestsMatching("com.pubnub.contract.*")
    }
    systemProperty("cucumber.filter.tags", System.getProperty("cucumber.filter.tags"))
    systemProperty("cucumber.features", System.getProperty("cucumber.features"))
    systemProperty("cucumber.plugins", System.getProperty("cucumber.plugins"))
}
