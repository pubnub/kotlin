package com.pubnub.api.integration

import com.pubnub.api.CommonUtils.randomChannel
import com.pubnub.internal.PubNubImpl
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.files.PNFileUploadResult
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
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
    fun uploadAsyncAndDelete() {
        val channel: String = randomChannel()
        val content = "This is content"
        val message = "This is message"
        val meta = "This is meta"
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
                meta = meta
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
            fileId = sendResult.file.id
        ).sync()
    }

    private fun uploadListDownloadDelete(withCipher: Boolean) {
        if (withCipher) {
            pubnub.configuration.cipherKey = "enigma"
        }
        val channel: String = randomChannel()
        val content = "This is content"
        val message = "This is message"
        val meta = "This is meta"
        val fileName = "fileName$channel.txt"
        val connectedLatch = CountDownLatch(1)
        val fileEventReceived = CountDownLatch(1)
        pubnub.addListener(object : LimitedListener() {
            override fun status(pubnub: PubNubImpl, pnStatus: PNStatus) {
                if (pnStatus.category == PNStatusCategory.Connected) {
                    connectedLatch.countDown()
                }
            }

            override fun file(pubnub: PubNubImpl, event: PNFileEventResult) {
                if (event.file.name == fileName) {
                    fileEventReceived.countDown()
                }
            }
        })
        pubnub.subscribe(channels = listOf(channel))
        connectedLatch.await(10, TimeUnit.SECONDS)
        val sendResult: PNFileUploadResult? = ByteArrayInputStream(content.toByteArray(StandardCharsets.UTF_8)).use {
            pubnub.sendFile(
                channel = channel,
                fileName = fileName,
                inputStream = it,
                message = message,
                meta = meta
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
        val (_, byteStream) = pubnub.downloadFile(
            channel = channel,
            fileName = fileName,
            fileId = sendResult.file.id
        ).sync()

        byteStream?.use {
            Assert.assertEquals(content, readToString(it))
        }
        pubnub.deleteFile(
            channel = channel,
            fileName = fileName,
            fileId = sendResult.file.id
        ).sync()
    }

    private fun readToString(inputStream: InputStream): String {
        Scanner(inputStream).useDelimiter("\\A").use { s -> return if (s.hasNext()) s.next() else "" }
    }

    private abstract class LimitedListener : com.pubnub.api.callbacks.SubscribeCallback() {
        override fun presence(pubnub: PubNubImpl, event: PNPresenceEventResult) {}
        override fun message(pubnub: PubNubImpl, event: PNMessageResult) {}
        override fun signal(pubnub: PubNubImpl, event: PNSignalResult) {}
        override fun objects(pubnub: PubNubImpl, event: PNObjectEventResult) {}
        override fun messageAction(pubnub: PubNubImpl, event: PNMessageActionResult) {}
    }
}
