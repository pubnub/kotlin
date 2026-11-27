@file:Suppress("ktlint")

package com.pubnub.docs.fileSharing

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.files.PNDownloadFileResult
import com.pubnub.api.models.consumer.files.PNFileUrlResult
import com.pubnub.api.models.consumer.files.PNListFilesResult
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.Kit.storedItem
import com.pubnub.docs.support.Kit.subscribeAndConnect
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.MessagingKit
import com.pubnub.docs.support.MessagingKit.assertFixtureAt
import com.pubnub.docs.support.MessagingKit.awaitListed
import com.pubnub.docs.support.MessagingKit.fixtureBytes
import com.pubnub.docs.support.MessagingKit.upload
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Stdout
import com.pubnub.docs.support.Wait
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live tests of the Kotlin docs page files (docs/sdks/kotlin/api-reference/files.md). One test per snippet. The text between a
 * start marker and `snippet.end` is the published sample, the arrangement is above it and the assertions below it. The
 * samples name a file by a placeholder id (`someFileId`, `d9515cb7-...`), the recorder swaps it for the id of the file that the
 * test uploaded.
 */
@ExtendWith(LiveExtension::class)
class FilesDocsTest {
    /** Deletes every file of [channel] after the test (the Main sample uploads a file the test does not know yet). */
    private fun cleanFiles(client: PubNub, channel: String) {
        Cleanup.cleanupAfterTest("delete the files of $channel") {
            for (file in client.listFiles(channel).sync().data) {
                client.deleteFile(channel, file.name, file.id).sync()
            }
        }
    }

    @Test
    fun filesBasicUsage(clients: LiveClients) {
        val receiver = clients.kotlinClient(OPEN, "docs-receiver")
        val arrived =
            Wait.expectEvent(receiver, "file") { event ->
                event is PNFileEventResult && event.channel == "demo-file-channel" && event.file.name == "test_document.txt"
            }
        subscribeAndConnect(receiver, listOf("demo-file-channel"))
        cleanFiles(receiver, "demo-file-channel")
        Cleanup.record("history", "demo-file-channel")
        val out = Stdout.captureForTest()

        com.pubnub.docs.fileSharing.samples.main()

        val event = arrived.await() as PNFileEventResult
        assertEquals("file-upload-demo-user", event.publisher)
        assertEquals("file-message", event.customMessageType)
        assertEquals("Here's a test document!", event.jsonMessage.asJsonObject["text"].asString)
        assertEquals("document", event.jsonMessage.asJsonObject["type"].asString)
        out.assertPrinted("File upload successful!")
        out.assertPrinted("  • Timetoken: ${event.timetoken}")
        out.assertPrinted("  • File ID: ${event.file.id}")
        out.assertPrinted("  • File Name: test_document.txt")
        val urlLine = out.lines("  • Download URL: ").singleOrNull()
        assertTrue(urlLine != null && urlLine.contains(event.file.id), "the printed download url does not hold the id of the file")
        val (status, body) = MessagingKit.get(urlLine!!.substringAfter("  • Download URL: ").trim())
        assertEquals(200, status, "the printed download url did not answer 200")
        assertEquals("This is a test file for PubNub file upload demo.", String(body, Charsets.UTF_8))
        assertTrue(out.lines("Error").isEmpty(), "the sample reported an error")
        awaitListed(receiver, "demo-file-channel", event.file.id)
        val stored = storedItem(receiver, "demo-file-channel", event.timetoken!!)
        assertEquals("demo-user", stored.meta!!.asJsonObject["sender"].asString)
        assertEquals("kotlin-example", stored.meta!!.asJsonObject["app"].asString)
    }

    @Test
    fun deleteFileBasicUsage(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val owner = clients.kotlinClient(OPEN, "docs-receiver")
        val file = upload(owner, "my_channel", "cat_picture.jpg")
        Cleanup.record("history", "my_channel")
        awaitListed(owner, "my_channel", file.id)
        recorder.placeholder("someFileId", file.id)

        // snippet.deleteFileBasicUsage
        pubnub.deleteFile(
            channel = "my_channel",
            fileName = "cat_picture.jpg",
            fileId = "someFileId"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        recorder.awaitOk("deleteFile")
        Wait.settled("the deleted file is no longer listed") {
            val ids = owner.listFiles("my_channel").sync().data.map { it.id }
            assertTrue(file.id !in ids, "the file is still listed after deleteFile")
        }
    }

    @Test
    fun downloadFileBasicUsage(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val owner = clients.kotlinClient(OPEN, "docs-receiver")
        val file = upload(owner, "my_channel", "cat_picture.jpg")
        Cleanup.record("history", "my_channel")
        awaitListed(owner, "my_channel", file.id)
        recorder.placeholder("d9515cb7-48a7-41a4-9284-f4bf331bc770", file.id)

        // snippet.downloadFileBasicUsage
        pubnub.downloadFile(
            channel = "my_channel",
            fileName = "cat_picture.jpg",
            fileId = "d9515cb7-48a7-41a4-9284-f4bf331bc770"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val downloaded = recorder.awaitOk("downloadFile") as PNDownloadFileResult
        assertEquals("cat_picture.jpg", downloaded.fileName)
        assertTrue(fixtureBytes().contentEquals(downloaded.byteStream!!.use { it.readBytes() }), "the downloaded bytes differ from the uploaded fixture")
    }

    @Test
    fun getFileUrlBasicUsage(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val owner = clients.kotlinClient(OPEN, "docs-receiver")
        val file = upload(owner, "my_channel", "cat_picture.jpg")
        Cleanup.record("history", "my_channel")
        awaitListed(owner, "my_channel", file.id)
        recorder.placeholder("someFileId", file.id)

        // snippet.getFileUrlBasicUsage
        pubnub.getFileUrl(
            channel = "my_channel",
            fileName = "cat_picture.jpg",
            fileId = "someFileId"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val url = (recorder.awaitOk("getFileUrl") as PNFileUrlResult).url
        assertTrue(url.contains("/my_channel/") && url.contains(file.id) && url.contains("cat_picture.jpg"), "the url does not name the channel, the id and the file name")
        assertFixtureAt(url)
    }

    @Test
    fun listFiles(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val owner = clients.kotlinClient(OPEN, "docs-receiver")
        val file = upload(owner, "my_channel", "cat_picture.jpg")
        Cleanup.record("history", "my_channel")
        awaitListed(owner, "my_channel", file.id)

        // snippet.listFiles
        pubnub.listFiles(
            channel = "my_channel"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val listed = recorder.awaitOk("listFiles") as PNListFilesResult
        val entry = listed.data.single { it.id == file.id }
        assertEquals("cat_picture.jpg", entry.name)
        assertEquals(fixtureBytes().size, entry.size)
        assertEquals(200, listed.status)
    }

    @Test
    fun publishFileMessageBasicUsage(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val owner = clients.kotlinClient(OPEN, "docs-receiver")
        val file = upload(owner, "my_channel", "cat_picture.jpg")
        Cleanup.record("history", "my_channel")
        awaitListed(owner, "my_channel", file.id)
        recorder.placeholder("d9515cb7-48a7-41a4-9284-f4bf331bc770", file.id)
        val receiver = clients.kotlinClient(OPEN, "docs-joiner")
        val arrived =
            Wait.expectEvent(receiver, "file") { event ->
                event is PNFileEventResult && event.channel == "my_channel" && event.jsonMessage.toString().contains("This is a sample message")
            }
        subscribeAndConnect(receiver, listOf("my_channel"))

        // snippet.publishFileMessageBasicUsage
        pubnub.publishFileMessage(
            channel = "my_channel",
            fileName = "cat_picture.jpg",
            fileId = "d9515cb7-48a7-41a4-9284-f4bf331bc770",
            message = "This is a sample message",
            customMessageType = "file-message"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val published = recorder.awaitOk("publishFileMessage") as PNPublishFileMessageResult
        val event = arrived.await() as PNFileEventResult
        assertEquals(published.timetoken, event.timetoken)
        assertEquals(file.id, event.file.id)
        assertEquals("cat_picture.jpg", event.file.name)
        assertEquals("myUserId", event.publisher)
        assertEquals("file-message", event.customMessageType)
        assertEquals("This is a sample message", event.jsonMessage.asString)
        storedItem(receiver, "my_channel", published.timetoken)
    }

    private companion object {
        const val OPEN = "open"
    }
}
