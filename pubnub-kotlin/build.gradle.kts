import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    `java-library`
    jacoco
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.benmanes.versions)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.lombok)
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val integrationTestImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
    extendsFrom(configurations.testImplementation.get())
}

tasks.named<Test>("test") {
    failFast = true
    useJUnitPlatform()
    exclude("**/contract/*.class")
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
    implementation(project(":pubnub-core:pubnub-core-impl"))

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

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets.configureEach {
        perPackageOption {
            matchingRegex.set(".*internal.*")
            suppress.set(true)
        }
        skipEmptyPackages.set(true)
    }
}

tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
        perPackageOption {
            matchingRegex.set(".*internal.*")
            suppress.set(true)
        }
        skipEmptyPackages.set(true)
    }
}

// tasks.register("mergeKotlinJars", org.gradle.jvm.tasks.Jar) {
//    from(zipTree("/Users/wojciech.kalicinski/projects/pubnub-kotlin-copy/pubnub-kotlin/build/libs/pubnub-kotlin-9.0.0.jar"), zipTree("/Users/wojciech.kalicinski/projects/pubnub-kotlin-copy/pubnub-core/pubnub-core-api/build/libs/pubnub-core-api-9.0.0.jar"))
//    setArchiveFileName("combined.jar")
// }
