package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register

class PubNubIntegrationTestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<JavaLibraryPlugin>()

            val integrationTestTask = tasks.register<Test>("integrationTest") {
                description = "Runs integration tests."
                group = "verification"

                useJUnitPlatform()
                testLogging {
                    it.events("passed")
                }
            }

            configure<SourceSetContainer> {
                val integrationTestSourceSet = create("integrationTest") {
                    it.compileClasspath += getByName("main").output
                    it.runtimeClasspath += getByName("main").output
                }
                integrationTestTask.configure {
                    it.testClassesDirs = integrationTestSourceSet.output.classesDirs
                    it.classpath = integrationTestSourceSet.runtimeClasspath
                }
            }

            configurations.getByName("integrationTestImplementation") {
                it.extendsFrom(configurations.getByName("implementation"))
                it.extendsFrom(configurations.getByName("testImplementation"))
            }
        }
    }
}