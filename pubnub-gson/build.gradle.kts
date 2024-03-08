plugins {
    checkstyle
    alias(libs.plugins.benmanes.versions)
    alias(libs.plugins.lombok)
    id("pubnub.shared")
    id("pubnub.dokka")
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
    testImplementation(libs.mockk)

    integrationTestImplementation(libs.owner)
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

//
// tasks.register("mergeKotlinJars", org.gradle.jvm.tasks.Jar) {
//    from(zipTree("/Users/wojciech.kalicinski/projects/pubnub-kotlin-copy/pubnub-gson/build/libs/pubnub-gson-9.0.0.jar"), zipTree("/Users/wojciech.kalicinski/projects/pubnub-kotlin-copy/pubnub-core/pubnub-core-api/build/libs/pubnub-core-api-9.0.0.jar"))
//    setArchiveFileName("combined.jar")
// }
