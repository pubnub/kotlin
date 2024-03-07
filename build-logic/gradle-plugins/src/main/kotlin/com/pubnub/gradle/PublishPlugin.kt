package com.pubnub.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.plugins.signing.SigningPlugin

class PublishPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply<MavenPublishPlugin>()
                apply<SigningPlugin>()
            }

//            afterEvaluate {
//                extensions.getByType<PublishingExtension>().publications.all {
//                    println(it.name + " " + it)
//                    if (it is MavenPublication) {
//                        println(it.artifacts)
//                    }
//                }
//            }
        }
    }
}