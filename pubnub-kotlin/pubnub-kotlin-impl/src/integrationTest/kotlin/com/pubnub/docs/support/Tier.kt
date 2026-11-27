package com.pubnub.docs.support

import java.io.File

/**
 * Where the files of the live docs tier are. The Gradle test task runs in the module directory of either SDK module, so
 * the repository root is found by walking up to `settings.gradle.kts`.
 */
object Tier {
    const val KOTLIN_TIER_PATH = "pubnub-kotlin/pubnub-kotlin-impl/src/integrationTest/kotlin/com/pubnub/docs"
    const val JAVA_TIER_PATH = "pubnub-gson/pubnub-gson-impl/src/integrationTest/java/com/pubnub/docs"

    val repoRoot: File by lazy {
        generateSequence(File("").absoluteFile) { it.parentFile }
            .firstOrNull { File(it, "settings.gradle.kts").isFile && File(it, KOTLIN_TIER_PATH).isDirectory }
            ?: error("The repository root was not found above ${File("").absoluteFile.name}")
    }

    /** The Kotlin tier folder, holds README, groups.json, coverage.json and the other data files. */
    val dir: File get() = File(repoRoot, KOTLIN_TIER_PATH)

    val javaDir: File get() = File(repoRoot, JAVA_TIER_PATH)

    fun file(name: String): File = File(dir, name)
}
