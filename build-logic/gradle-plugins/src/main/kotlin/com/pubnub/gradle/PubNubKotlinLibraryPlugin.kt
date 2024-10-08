package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
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

            tasks.withType<AbstractTestTask> {
                testLogging {
                    it.showStackTraces = true
                    it.showCauses = true
                    it.showExceptions = true
                    it.exceptionFormat = TestExceptionFormat.FULL
                }
//                if (providers.environmentVariable("CI").isPresent && !providers.environmentVariable("CI_FORCE_RUN_INTEGRATION_TESTS").isPresent) {
//                    filter.excludeTestsMatching("com.pubnub.test.integration.*")
//                }
            }

        }
    }
}