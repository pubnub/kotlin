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
    id("pubnub.shared")
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

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
    api(libs.retrofit2)
    api(libs.okhttp)
    api(libs.okhttp.logging)
    api(libs.json)
    api(libs.gson)
    implementation(libs.slf4j)
    ktlintRuleset(project(":build-logic:ktlint-custom-rules"))
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
