package com.pubnub.api.integration

import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.util.concurrent.TimeUnit

class PublishFileMessageIntegrationTests : BaseIntegrationTest() {
    @Test
    fun canPublishFileMessage() {
        val publishFileMessageResult: PNPublishFileMessageResult = pubnub.publishFileMessage(
            channel = "whatever",
            fileName = "whatever",
            fileId = "whatever",
            message = "whatever",
            meta = "whatever",
            ttl = 1,
            shouldStore = true,
            customMessageType = "myCustom-Messag_e"
        ).sync()
        assertNotNull(publishFileMessageResult.timetoken)
    }

    @Test
    fun publishFileMessageAndReceiveOnListener() {
        val channelName = com.pubnub.test.CommonUtils.randomChannel()
        val fileName = "fileName_24channelName.txt"
        val messageSendFile = "This is a file message"
        val publishFileMessage = "This is a publishFileMessage"
        val customMessageType = "file-message"
        val content = "This is the file content"
        val receivedFileEventFromSendFile = java.util.concurrent.CountDownLatch(1)
        val receivedFileEventFromPublishFileMessage = java.util.concurrent.CountDownLatch(1)

        val channel = pubnub.channel(channelName)
        val subscription = channel.subscription()
        subscription.onFile = { pnFileEventResult ->
            println("Received file event: $pnFileEventResult")
            println("message: ${pnFileEventResult.message}")
            if (pnFileEventResult.message == messageSendFile) {
                println("-=o1")
                assertEquals(fileName, pnFileEventResult.file.name)
                receivedFileEventFromSendFile.countDown()
            }
            if (pnFileEventResult.message == publishFileMessage) {
                println("-=o2")
                assertEquals(fileName, pnFileEventResult.file.name)
                receivedFileEventFromPublishFileMessage.countDown()
            }
        }
        subscription.subscribe()
        Thread.sleep(1000)

        // 1. Upload a file using sendFile to get a real fileId and fileName
        val uploadResult = ByteArrayInputStream(content.toByteArray(Charsets.UTF_8)).use {
            pubnub.sendFile(
                channel = channelName,
                fileName = fileName,
                inputStream = it,
                message = messageSendFile,
                customMessageType = customMessageType
            ).sync()
        }
        val fileId = uploadResult.file.id
        assertNotNull(fileId)

        // 2. Use those values in publishFileMessage
        val publishFileMessageResult = pubnub.publishFileMessage(
            channel = channelName,
            fileName = fileName,
            fileId = fileId,
            message = publishFileMessage,
            customMessageType = customMessageType
        ).sync()
        assertNotNull(publishFileMessageResult.timetoken)

        // 3. Wait for both file events
        try {
            assertTrue(receivedFileEventFromSendFile.await(10, TimeUnit.SECONDS))
            assertTrue(receivedFileEventFromPublishFileMessage.await(10, TimeUnit.SECONDS))
        } finally {
            // Cleanup: delete the uploaded file
            pubnub.deleteFile(
                channel = channelName,
                fileName = fileName,
                fileId = fileId,
            ).sync()
        }
    }
}
