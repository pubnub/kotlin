import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    `java-library`
    checkstyle
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

    integrationTestImplementation(libs.owner)
}

jacoco {
    toolVersion = "0.8.2"
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        html.required = true
    }
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

// check.dependsOn jacocoTestReport
//
// javadoc {
//    destinationDir = file("docs")
//    options.noTimestamp = true
// }

// task sourcesJar(type: Jar, dependsOn: classes) {
// //    classifier = 'sources'
//    from "$buildDir/delombok"
// }

tasks.named<Test>("test") {
    useJUnitPlatform()
    exclude("**/contract/*.class")
}

//
// tasks.register("mergeKotlinJars", org.gradle.jvm.tasks.Jar) {
//    from(zipTree("/Users/wojciech.kalicinski/projects/pubnub-kotlin-copy/pubnub-gson/build/libs/pubnub-gson-9.0.0.jar"), zipTree("/Users/wojciech.kalicinski/projects/pubnub-kotlin-copy/pubnub-core/pubnub-core-api/build/libs/pubnub-core-api-9.0.0.jar"))
//    setArchiveFileName("combined.jar")
// }
