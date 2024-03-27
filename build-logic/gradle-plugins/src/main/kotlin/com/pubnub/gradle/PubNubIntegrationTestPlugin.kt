package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class PubNubIntegrationTestPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<JavaLibraryPlugin>()

            configure<SourceSetContainer> {
                create("integrationTest") {
                    it.compileClasspath += getByName("main").output
                    it.runtimeClasspath += getByName("main").output
                }
            }

            configurations.getByName("integrationTestImplementation") {
                it.extendsFrom(configurations.getByName("implementation"))
                it.extendsFrom(configurations.getByName("testImplementation"))
            }
        }
    }
}