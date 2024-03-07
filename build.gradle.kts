plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.benmanes.versions) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.gradle.nexus.publish)
}


nexusPublishing {
    repositories {
        sonatype()
    }
}