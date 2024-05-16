package com.pubnub.gradle

import com.vanniktech.maven.publish.MavenPublishPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class PubNubSharedPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<MavenPublishPlugin>()

            group = providers.gradleProperty("GROUP").get()
            version = providers.gradleProperty("VERSION_NAME").get()
        }
    }
}