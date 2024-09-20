package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport

class PubNubTestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<JavaLibraryPlugin>()
            apply<JacocoPlugin>()

            // Tests
            tasks.named<Test>("test").configure { test ->
                test.failFast = true
                test.exclude("**/contract/*.class")
                test.useJUnitPlatform()
            }

            tasks.withType<JacocoReport>().configureEach { task ->
                task.reports {
                    it.xml.required.set(true)
                    it.html.required.set(true)
                }
            }
        }
    }
}