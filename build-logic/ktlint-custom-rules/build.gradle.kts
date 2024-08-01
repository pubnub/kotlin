plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    compileOnly("com.pinterest.ktlint:ktlint-rule-engine-core:1.2.1")
    compileOnly("com.pinterest.ktlint:ktlint-cli-ruleset-core:1.2.1")
}

group = "com.pubnub"
version = "1.0.0"