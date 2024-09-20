package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial

class PubNubDokkaPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<DokkaPlugin>()

            tasks.withType<DokkaTask>().configureEach { task ->
                task.notCompatibleWithConfigurationCache("https://github.com/Kotlin/dokka/issues/2231")
                task.dokkaSourceSets.configureEach { sourceSetBuilder ->
                    sourceSetBuilder.perPackageOption {
                        it.matchingRegex.set(".*internal.*")
                        it.suppress.set(true)
                    }
                    sourceSetBuilder.skipEmptyPackages.set(true)
                }
            }

            tasks.withType<DokkaTaskPartial>().configureEach { task ->
                task.dokkaSourceSets.configureEach {
                    it.perPackageOption {
                        it.matchingRegex.set(".*internal.*")
                        it.suppress.set(true)
                    }
                    it.skipEmptyPackages.set(true)
                }
            }
        }
    }
}