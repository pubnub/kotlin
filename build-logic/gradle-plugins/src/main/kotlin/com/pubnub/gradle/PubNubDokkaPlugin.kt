package com.pubnub.gradle

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.DokkaSourceSetSpec

class PubNubDokkaPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.dokka")

        // Dokka 2.x: configure via DokkaExtension
        extensions.configure<DokkaExtension> {
            // Access dokkaSourceSets - it's a property on DokkaExtension
            val sourceSets: NamedDomainObjectContainer<DokkaSourceSetSpec> = dokkaSourceSets

            // Applies to all Dokka source sets that exist (per project / per target)
            sourceSets.configureEach { sourceSet ->
                // Skip packages with no visible declarations
                sourceSet.skipEmptyPackages.set(true)

                // Suppress packages matching regex pattern
                sourceSet.perPackageOption {
                    it.matchingRegex.set(".*internal.*")
                    it.suppress.set(true)
                }
            }
        }
    }
}
