package com.pubnub.migration

import com.github.ajalt.clikt.core.main
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MigrationTest {
    private val replacements = Migration.loadReplacementsFile(this::class.java.getResourceAsStream("/testReplacements.txt")!!)

    @Test
    fun loadReplacements() {
        assertTrue { replacements.size == 2 }
//        replacements.forEach {
//            assertEquals(it.first, it.second.replace(".java", ""))
//        }
    }

    @Test
    fun replaceLine() {
        val inputLine = "xaaax aaa; zzz ddd ccc ccC AaA"
        val expectedLine = "xaaax bbb; zzz ddd ddd ccC AaA"

        val line = Migration.replaceLine(inputLine, replacements)

        assertEquals(expectedLine, line)
    }

    @Test
    fun replaceLines_changed_lines_returned() {
        val inputLines = sequenceOf(
            "xaaax aaa; zzz ddd ccc ccC AaA",
            "bbb",
            "ccc aaa ddd",
            "aaa bbb ccc"
        )
        val expectedLines = """
            xaaax bbb; zzz ddd ddd ccC AaA
            bbb
            ddd bbb ddd
            bbb bbb ddd
            
        """.trimIndent()
        val lines = Migration.replaceLines(inputLines, replacements)

        assertNotNull(lines)
        assertEquals(expectedLines, lines)
    }

    @Test
    fun replaceLines_no_change_result_is_null() {
        val inputLines = sequenceOf(
            "zzz",
            "zzz",
            "zzz",
            "zzz"
        )
        val lines = Migration.replaceLines(inputLines, replacements)

        assertNull(lines)
    }

    @Test
    fun dry_run_files_unchanged() {
        val tmpDir = createTmpTestDataFolder()
        // for dry run we compare with test data itself:
        val goldenDataDir = File("src/test/resources/testData")

        Migration(replacements).main(listOf("--dry-run", "--no-backups", tmpDir.path))
        assertDirContentEquals(goldenDataDir, tmpDir)
    }

    @Test
    fun no_dry_run_files_changed() {
        val tmpDir = createTmpTestDataFolder()
        val goldenDataDir = File("src/test/resources/goldenData")

        Migration(replacements).main(listOf("--no-backups", tmpDir.path))
        assertDirContentEquals(goldenDataDir, tmpDir)
    }
}

private fun createTmpTestDataFolder(): File {
    val tmpDir = Files.createTempDirectory(null).toFile()
    tmpDir.deleteOnExit()
    File("src/test/resources/testData").copyRecursively(tmpDir)
    return tmpDir
}

private fun assertDirContentEquals(expectedDir: File, actualDir: File) {
    actualDir.walkBottomUp().forEach { file ->
        if (file.isFile) {
            val relative = file.relativeTo(actualDir)
            val goldenFile = expectedDir.resolve(relative)
            assertEquals(goldenFile.readText(), file.readText())
        }
        file.deleteOnExit()
    }
}