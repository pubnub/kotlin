plugins {
    alias(libs.plugins.benmanes.versions)
    id("pubnub.shared")
    id("pubnub.dokka")
}

dependencies {
    api(libs.retrofit2)
    api(libs.okhttp)
    api(libs.okhttp.logging)
    api(libs.json)
    api(libs.gson)
    implementation(libs.slf4j)
}
