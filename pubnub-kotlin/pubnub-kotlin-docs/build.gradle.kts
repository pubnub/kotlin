// Dokka aggregator for the API reference of both SDKs. The docs code samples live in the live docs snippet tier
// (pubnub-kotlin-impl and pubnub-gson-impl src/integrationTest, package com.pubnub.docs).
plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.kotlin-library")
    id("pubnub.dokka")
    id("pubnub.shared")
}

dependencies {
    // Dokka V2: Aggregate documentation from all public API modules
    // Kotlin SDK modules
    dokka(project(":pubnub-kotlin:pubnub-kotlin-api"))
    dokka(project(":pubnub-kotlin:pubnub-kotlin-core-api"))
    dokka(project(":pubnub-kotlin:pubnub-kotlin-coroutines"))
    // Java/Gson SDK modules
    dokka(project(":pubnub-gson:pubnub-gson-api"))
    dokka(project(":pubnub-gson:pubnub-gson-impl"))
}
