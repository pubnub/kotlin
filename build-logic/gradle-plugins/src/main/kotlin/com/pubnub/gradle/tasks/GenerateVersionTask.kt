package com.pubnub.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
abstract class GenerateVersionTask : DefaultTask() {

    @get:Input
    abstract val version: Property<String>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun run() {
        val outputDir = outputDirectory.get().asFile
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw IllegalStateException("Can't create output directory $outputDir")
        }
        val versionString = version.get()
        if (versionString.isBlank()) {
            throw IllegalStateException("Version can't be blank. Check gradle.properties VERSION_NAME= property")
        }

        File(outputDir, "PubNubVersion.kt").writeText("""
            package com.pubnub.internal
            
            internal const val PUBNUB_VERSION = "$versionString"

        """.trimIndent())
    }
}