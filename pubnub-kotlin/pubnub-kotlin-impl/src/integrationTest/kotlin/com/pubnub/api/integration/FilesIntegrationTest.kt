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
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.Scanner
import java.util.concurrent.CompletableFuture
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
    fun legacyEncryptedFileTransfer() {
        uploadListDownloadDeleteFileWithCipher(true)
    }

    @Test
    fun aesCbcEncryptedFileTransfer() {
        uploadListDownloadDeleteFileWithCipher(false)
    }

    fun uploadListDownloadDeleteFileWithCipher(withLegacyCrypto: Boolean) {
        if (withLegacyCrypto) {
            clientConfig = {
                cryptoModule = CryptoModule.createLegacyCryptoModule("enigma")
            }
        } else {
            clientConfig = {
                cryptoModule = CryptoModule.createAesCbcCryptoModule("enigma")
            }
        }

        val channel: String = randomChannel()
        val fileName = "logback.xml"
        val message = "This is message"
        val meta = "This is meta"
        val customMessageType = "myCustomType"

        // Read the logback.xml file from resources
        val logbackResource = this.javaClass.classLoader.getResourceAsStream("logback.xml")
            ?: throw IllegalStateException("logback.xml not found in resources")
        val originalContent = logbackResource.readBytes()
        val originalContentString = String(originalContent, StandardCharsets.UTF_8)

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
            ByteArrayInputStream(originalContent).use {
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
            val downloadedContent = it.readBytes()
            val downloadedString = String(downloadedContent, StandardCharsets.UTF_8)
            Assert.assertEquals(
                "Downloaded content should match original logback.xml",
                originalContentString,
                downloadedString
            )
        }

        pubnub.deleteFile(
            channel = channel,
            fileName = fileName,
            fileId = sendResult.file.id,
        ).sync()
    }

    @Test
    fun testSendFileAndDeleteFileOnChannelEntity() {
        val sendFileResultReference: AtomicReference<PNFileUploadResult> = AtomicReference()
        val fileSent = CountDownLatch(1)
        val channelName: String = randomChannel()
        val fileName = "fileName$channelName.txt"
        val message = "This is a message"
        val meta = "This is meta"
        val content = "This is content"
        val customMessageType = "MyCustom-Type_"
        val receiveFileEventFuture = CompletableFuture<PNFileEventResult>()

        val channel = pubnub.channel(channelName)

        val subscription = channel.subscription()
        subscription.onFile = { fileEvent: PNFileEventResult ->
            receiveFileEventFuture.complete(fileEvent)
        }
        subscription.subscribe()
        Thread.sleep(1000)

        ByteArrayInputStream(content.toByteArray(StandardCharsets.UTF_8)).use {
            channel.sendFile(
                fileName = fileName,
                inputStream = it,
                message = message,
                meta = meta,
                customMessageType = customMessageType
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

        val fileEvent = receiveFileEventFuture.get(10, TimeUnit.SECONDS)
        assertEquals(customMessageType, fileEvent.customMessageType)

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

    @Test
    fun uploadLargeEncryptedFileWithLegacyCryptoModule() {
        uploadLargeEncryptedFileWithCryptoModule(withLegacyCrypto = true)
    }

    @Test
    fun uploadLargeEncryptedFileWithAesCbcCryptoModule() {
        uploadLargeEncryptedFileWithCryptoModule(withLegacyCrypto = false)
    }

    fun uploadLargeEncryptedFileWithCryptoModule(withLegacyCrypto: Boolean) {
        clientConfig = {
            cryptoModule = CryptoModule.createLegacyCryptoModule("enigma")
        }
        val channel: String = randomChannel()
        val fileName = "large_file_${System.currentTimeMillis()}.bin"

        // Create a large binary file (1MB) to test encryption
        val largeContent = ByteArray(1024 * 1024) { it.toByte() }

        val sendResult: PNFileUploadResult? =
            ByteArrayInputStream(largeContent).use {
                pubnub.sendFile(
                    channel = channel,
                    fileName = fileName,
                    inputStream = it,
                    message = "Large encrypted file test",
                ).sync()
            }

        if (sendResult == null) {
            Assert.fail("Failed to upload large encrypted file")
            return
        }

        // Download and verify
        val (_, byteStream) =
            pubnub.downloadFile(
                channel = channel,
                fileName = fileName,
                fileId = sendResult.file.id,
            ).sync()

        byteStream?.use {
            val downloadedContent = it.readBytes()
            Assert.assertArrayEquals(
                "Downloaded encrypted content should match original",
                largeContent,
                downloadedContent
            )
        }

        // Cleanup
        pubnub.deleteFile(
            channel = channel,
            fileName = fileName,
            fileId = sendResult.file.id,
        ).sync()
    }

    @Test
    fun uploadMultipleSizesWithEncryption() {
        clientConfig = {
            cryptoModule = CryptoModule.createLegacyCryptoModule("enigma")
        }
        val channel: String = randomChannel()

        val testSizes = listOf(
            100, // Small file
            1024, // 1KB
            10240, // 10KB
            102400, // 100KB
            524288 // 512KB
        )

        for (size in testSizes) {
            val fileName = "test_${size}_${System.currentTimeMillis()}.bin"
            val content = ByteArray(size) { (it % 256).toByte() }

            val sendResult: PNFileUploadResult? =
                ByteArrayInputStream(content).use {
                    pubnub.sendFile(
                        channel = channel,
                        fileName = fileName,
                        inputStream = it,
                        message = "Test file size: $size",
                    ).sync()
                }

            if (sendResult == null) {
                Assert.fail("Failed to upload file of size $size")
                return
            }

            // Download and verify
            val (_, byteStream) =
                pubnub.downloadFile(
                    channel = channel,
                    fileName = fileName,
                    fileId = sendResult.file.id,
                ).sync()

            byteStream?.use {
                val downloadedContent = it.readBytes()
                Assert.assertArrayEquals(
                    "Downloaded content should match original for size $size",
                    content,
                    downloadedContent
                )
            }

            // Cleanup
            pubnub.deleteFile(
                channel = channel,
                fileName = fileName,
                fileId = sendResult.file.id,
            ).sync()
        }
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
