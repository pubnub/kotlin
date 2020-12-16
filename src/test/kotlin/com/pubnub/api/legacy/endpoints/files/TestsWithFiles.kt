package com.pubnub.api.legacy.endpoints.files

import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.ArrayList
import java.util.Collections

interface TestsWithFiles {
    val temporaryFolder: TemporaryFolder

    fun getTemporaryFile(filename: String, vararg content: String?): File {
        return try {
            val file = temporaryFolder.newFile(filename)
            val lines = ArrayList<String>()
            Collections.addAll<String>(lines, *content)
            Files.write(file.toPath(), lines)
            file
        } catch (ex: IOException) {
            // fail
            throw RuntimeException(ex.message, ex)
        }
    }

    companion object {
        val folder = TemporaryFolder()
    }
}
