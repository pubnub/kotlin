plugins {
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

// tasks.register("mergeKotlinJars", org.gradle.jvm.tasks.Jar) {
//    from(zipTree("/Users/wojciech.kalicinski/projects/pubnub-kotlin-copy/pubnub-kotlin/build/libs/pubnub-kotlin-9.0.0.jar"), zipTree("/Users/wojciech.kalicinski/projects/pubnub-kotlin-copy/pubnub-core/pubnub-core-api/build/libs/pubnub-core-api-9.0.0.jar"))
//    setArchiveFileName("combined.jar")
// }
