package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.files.PNFileUploadResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.test.CommonUtils.randomChannel
import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.Scanner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

class FilesIntegrationTest : BaseIntegrationTest() {
    @Test
    fun uploadListDownloadDeleteWithCipher() {
        uploadListDownloadDelete(true)
    }

    @Test
    fun uploadListDownloadDeleteWithoutCipher() {
        uploadListDownloadDelete(false)
    }

    @Test
    fun testSendFileAndDeleteFileOnChannelEntity() {
        val sendFileResultReference: AtomicReference<PNFileUploadResult> = AtomicReference()
        val fileSent = CountDownLatch(1)
        val channelName: String = randomChannel()
        val fileName = "fileName$channelName.txt"
        val message = "This is message"
        val meta = "This is meta"
        val content = "This is content"

        val channel = pubnub.channel(channelName)
        ByteArrayInputStream(content.toByteArray(StandardCharsets.UTF_8)).use {
            channel.sendFile(
                fileName = fileName,
                inputStream = it,
                message = message,
                meta = meta,
            ).async { result ->
                result.onSuccess {
                    sendFileResultReference.set(it)
                }
                fileSent.countDown()
            }
        }

        if (!fileSent.await(3, TimeUnit.SECONDS)) {
            Assert.fail()
            return
        }

        val sendFileResult = sendFileResultReference.get()
        if (sendFileResult == null) {
            Assert.fail()
            return
        }

        channel.deleteFile(
            fileName = fileName,
            fileId = sendFileResult.file.id,
        ).sync()
    }

    @Test
    fun uploadAsyncAndDelete() {
        val channel: String = randomChannel()
        val content = "This is content"
        val message = "This is message"
        val meta = "This is meta"
        val customMessageType = "MyCustomType"
        val fileName = "fileName$channel.txt"
        val fileSent = CountDownLatch(1)
        pubnub.subscribe(channels = listOf(channel))
        val sendResultReference: AtomicReference<PNFileUploadResult> = AtomicReference()
        ByteArrayInputStream(content.toByteArray(StandardCharsets.UTF_8)).use {
            pubnub.sendFile(
                channel = channel,
                fileName = fileName,
                inputStream = it,
                message = message,
                meta = meta,
                customMessageType = customMessageType
            ).async { result ->
                result.onSuccess {
                    sendResultReference.set(it)
                }
                fileSent.countDown()
            }
        }

        if (!fileSent.await(3, TimeUnit.SECONDS)) {
            Assert.fail()
            return
        }
        val sendResult = sendResultReference.get()

        if (sendResult == null) {
            Assert.fail()
            return
        }

        pubnub.deleteFile(
            channel = channel,
            fileName = fileName,
            fileId = sendResult.file.id,
        ).sync()
    }

    private fun uploadListDownloadDelete(withCipher: Boolean) {
        if (withCipher) {
            clientConfig = { cryptoModule = CryptoModule.createLegacyCryptoModule("enigma") }
        }
        val channel: String = randomChannel()
        val content = "This is content"
        val message = "This is message"
        val meta = "This is meta"
        val fileName = "fileName$channel.txt"
        val customMessageType = "myCustomType"
        val connectedLatch = CountDownLatch(1)
        val fileEventReceived = CountDownLatch(1)
        pubnub.addListener(
            object : SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    status: PNStatus,
                ) {
                    if (status.category == PNStatusCategory.PNConnectedCategory) {
                        connectedLatch.countDown()
                    }
                }

                override fun file(
                    pubnub: PubNub,
                    result: PNFileEventResult,
                ) {
                    if (result.file.name == fileName && result.customMessageType == customMessageType) {
                        fileEventReceived.countDown()
                    }
                }
            },
        )
        pubnub.subscribe(channels = listOf(channel))
        connectedLatch.await(10, TimeUnit.SECONDS)
        val sendResult: PNFileUploadResult? =
            ByteArrayInputStream(content.toByteArray(StandardCharsets.UTF_8)).use {
                pubnub.sendFile(
                    channel = channel,
                    fileName = fileName,
                    inputStream = it,
                    message = message,
                    meta = meta,
                    customMessageType = customMessageType
                ).sync()
            }

        if (sendResult == null) {
            Assert.fail()
            return
        }
        fileEventReceived.await(10, TimeUnit.SECONDS)
        val (_, _, _, data) = pubnub.listFiles(channel = channel).sync()
        val fileFoundOnList = data.find { it.id == sendResult.file.id } != null

        Assert.assertTrue(fileFoundOnList)
        val (_, byteStream) =
            pubnub.downloadFile(
                channel = channel,
                fileName = fileName,
                fileId = sendResult.file.id,
            ).sync()

        byteStream?.use {
            Assert.assertEquals(content, readToString(it))
        }
        pubnub.deleteFile(
            channel = channel,
            fileName = fileName,
            fileId = sendResult.file.id,
        ).sync()
    }

    private fun readToString(inputStream: InputStream): String {
        Scanner(inputStream).useDelimiter("\\A").use { s ->
            return if (s.hasNext()) {
                s.next()
            } else {
                ""
            }
        }
    }
}
