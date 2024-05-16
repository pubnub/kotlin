package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType

class PubNubJavaLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<PubNubSharedPlugin>()
            apply<JavaLibraryPlugin>()

            tasks.withType<JavaCompile>().configureEach {
                it.options.compilerArgs.add("-parameters")
            }
        }
    }
}