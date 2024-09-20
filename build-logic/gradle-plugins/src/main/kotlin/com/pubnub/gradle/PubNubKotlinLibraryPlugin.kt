package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class PubNubKotlinLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<PubNubSharedPlugin>()
            apply<KotlinPluginWrapper>()

            // Kotlin
            extensions.configure<KotlinJvmProjectExtension> {
                jvmToolchain(8)
            }

            tasks.named("compileKotlin", KotlinJvmCompile::class.java) {
                it.compilerOptions {
                    javaParameters.set(true)
                    freeCompilerArgs.add("-Xjvm-default=all")
                }
            }

        }
    }
}