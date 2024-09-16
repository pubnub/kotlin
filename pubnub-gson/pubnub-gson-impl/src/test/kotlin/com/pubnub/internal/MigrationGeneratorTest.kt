package com.pubnub.internal

import com.google.common.reflect.ClassPath
import org.junit.Test

class MigrationGeneratorTest {
    /**
     * Use this test to generate the content of the <root_project>/migration_utils/replacements.txt file
     */
    @Test
    fun generateMigrations() {
        ClassPath
            .from(ClassLoader.getSystemClassLoader())
            .allClasses
            .filter { it.packageName.startsWith("com.pubnub.api.java") }
            .mapNotNull { it.load().canonicalName }
            .sorted()
            .forEach {
                println(it.replace("com.pubnub.api.java.", "com.pubnub.api.") + ":" + it)
            }
    }
}
