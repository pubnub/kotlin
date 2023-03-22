package com.pubnub.api.integration

import com.google.gson.JsonObject
import com.pubnub.api.SpaceId
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.InputStream
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import org.hamcrest.Matchers.`is` as iz

class FilesIntegrationTest : BaseIntegrationTest() {

    @ParameterizedTest
    @ValueSource(strings = ["enigma", ""])
    fun sendListDownloadDelete(cipher: String) {
        pubnub.configuration.cipherKey = cipher
        val expectedSpaceId = SpaceId("thisIsSpace")
        val expectedType = "thisIsType"
        val expectedPayload = JsonObject().apply { addProperty("this_is_payload", "value") }
        val expectedMeta = JsonObject().apply { addProperty("this_is_meta", "value") }
        val fileName = "filename"
        val content = "This is the content"

        pubnub.doWhenSubscribedAndConnected { subscribeContext ->
            val sendFileResult = content.byteInputStream().use { stream ->
                pubnub.sendFile(
                    channel = subscribeContext.channel,
                    spaceId = expectedSpaceId,
                    type = expectedType,
                    message = expectedPayload,
                    fileName = fileName,
                    inputStream = stream,
                    meta = expectedMeta
                ).sync()
            }

            val fileEvent = subscribeContext.receivedFiles.pollOrThrow(3_000, TimeUnit.MILLISECONDS)

            assertThat(fileEvent.channel, iz(subscribeContext.channel))
            assertThat(fileEvent.type, iz(expectedType))
            assertThat(fileEvent.spaceId, iz(expectedSpaceId))
            assertThat(fileEvent.spaceId, iz(expectedSpaceId))
            assertThat(fileEvent.jsonMessage, iz(expectedPayload))
            assertThat(fileEvent.userMetadata, iz(expectedMeta))
            assertThat(fileEvent.file.name, iz(fileName))
            assertThat(fileEvent.file.id, iz(sendFileResult?.file?.id))

            val (_, _, _, data) = pubnub.listFiles(channel = subscribeContext.channel).sync()!!
            assertThat(data.map { it.id }, hasItem(fileEvent.file.id))

            val (_, byteStream) = pubnub.downloadFile(
                channel = subscribeContext.channel,
                fileName = fileName,
                fileId = fileEvent.file.id
            ).sync()!!
            byteStream?.use {
                assertThat(it.readToString(), iz(content))
            }

            pubnub.deleteFile(
                channel = subscribeContext.channel,
                fileName = fileName,
                fileId = fileEvent.file.id
            ).sync()!!
        }
    }

    private fun InputStream.readToString(): String {
        return readBytes().toString(Charset.defaultCharset())
    }
}
