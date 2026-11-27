package com.pubnub.docs.support

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.files.PNBaseFile
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.ByteArrayInputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Arrange helpers of the messaging pages (presence, channel groups, history, message actions, files, push), on Kotlin clients.
 * Java tests call them for their second clients. Nothing here is part of a sample.
 */
object MessagingKit {
    /** The bytes every uploaded fixture file carries (134 bytes of text, standing in for the picture of the docs). */
    private const val FIXTURE_TEXT =
        "This is the fixture of the live docs tests. It stands in for the picture that the file sharing samples upload. 0123456789"

    @JvmStatic
    fun fixtureBytes(): ByteArray = FIXTURE_TEXT.toByteArray(Charsets.UTF_8)

    /** The channels of [group], sorted. A group that does not exist lists nothing. */
    @JvmStatic
    fun groupChannels(
        client: PubNub,
        group: String,
    ): List<String> =
        try {
            client.listChannelsForChannelGroup(group).sync().channels.sorted()
        } catch (error: PubNubException) {
            if (error.statusCode == 404) {
                emptyList()
            } else {
                throw error
            }
        }

    /** Waits until the service lists exactly [expected] (any order) as the channels of [group]. */
    @JvmStatic
    fun awaitGroup(
        client: PubNub,
        group: String,
        vararg expected: String,
    ) {
        Wait.settled("the group $group lists ${expected.toList()}") {
            assertEquals(expected.toList().sorted(), groupChannels(client, group), "the channels of $group differ")
        }
    }

    /**
     * The Apple device token of the push samples. The service accepts a hex string of 64 or 40 characters for APNs, so the
     * tests swap the docs placeholder `appleDevice` for it at the client call (see `Recorder.placeholder`).
     */
    const val APPLE_TOKEN = "740f4707bebcf74f9b7c25d48e3358945f6aa01da5ddb387462c7eaf61bb78ad"

    /** The Google device token of the push samples (placeholder `googleDevice`). Any string works for FCM, hex works everywhere. */
    const val GOOGLE_TOKEN = "f1d8c4e07a39b2c65d1e8f3a4b7c9d0e2f6a8b1c3d5e7f90a2b4c6d8e0f1a3b5"

    private const val PUSH_TOPIC = "myapptopic"

    private fun pushTopic(type: PNPushType): String? {
        return if (type == PNPushType.APNS2) {
            PUSH_TOPIC
        } else {
            null
        }
    }

    /** The channels [device] is registered on for [type], sorted. The topic and environment of the samples apply to APNS2. */
    @JvmStatic
    fun pushChannels(
        client: PubNub,
        type: PNPushType,
        device: String,
    ): List<String> =
        client.auditPushChannelProvisions(type, device, pushTopic(type), PNPushEnvironment.DEVELOPMENT).sync().channels.sorted()

    /** Removes every registration of [device] for [type]. */
    @JvmStatic
    fun removeDevice(
        client: PubNub,
        type: PNPushType,
        device: String,
    ) {
        Retry.retryTransient(label = "remove push device") {
            client.removeAllPushNotificationsFromDeviceWithPushToken(type, device, pushTopic(type), PNPushEnvironment.DEVELOPMENT).sync()
        }
    }

    /** Waits until the service lists exactly [expected] for the device. */
    @JvmStatic
    fun awaitPush(
        client: PubNub,
        type: PNPushType,
        device: String,
        vararg expected: String,
    ) {
        Wait.settled("$type device is registered on ${expected.toList()}") {
            assertEquals(expected.toList().sorted(), pushChannels(client, type, device), "the channels of the device differ ($type)")
        }
    }

    /**
     * Brings [device] to a known state: no registration, then [channels] registered. The device is removed again after the
     * test.
     */
    @JvmStatic
    fun seedPush(
        client: PubNub,
        type: PNPushType,
        device: String,
        vararg channels: String,
    ) {
        removeDevice(client, type, device)
        Cleanup.cleanupAfterTest("remove push device") { removeDevice(client, type, device) }
        awaitPush(client, type, device)
        if (channels.isNotEmpty()) {
            Retry.retryTransient(label = "register push device") {
                client.addPushNotificationsOnChannels(
                    type,
                    channels.toList(),
                    device,
                    pushTopic(type),
                    PNPushEnvironment.DEVELOPMENT,
                ).sync()
            }
            awaitPush(client, type, device, *channels)
        }
    }

    /** Waits until [user] is listed as present on [channel] by the service. */
    @JvmStatic
    fun awaitPresent(
        client: PubNub,
        channel: String,
        user: String,
    ) {
        Wait.settled("$user is present on $channel") {
            val here = client.hereNow(channels = listOf(channel), includeUUIDs = true).sync()
            val who = here.channels[channel]?.occupants?.map { it.uuid }.orEmpty()
            assertTrue(user in who, "the user is not listed on $channel yet (listed: $who)")
        }
    }

    /**
     * A joiner client for [user] that is subscribed to [channels], carries [state] there, and is listed by the service with
     * that state. Nobody else may be present on the channels, the occupancy of a sample is then exact.
     */
    @JvmStatic
    fun joinWithState(
        clients: LiveClients,
        user: String,
        channels: List<String>,
        state: Map<String, Any>,
    ): PubNub {
        val reader = clients.kotlinClient("open", "docs-receiver")
        Kit.awaitNoOccupants(reader, *channels.toTypedArray())
        val joiner = clients.kotlinClient("open", user)
        Kit.subscribeAndConnect(joiner, channels)
        joiner.setPresenceState(channels = channels, state = state).sync()
        for (channel in channels) {
            awaitPresent(reader, channel, user)
        }
        Wait.settled("the state of $user is visible on $channels") {
            val here = reader.hereNow(channels = channels, includeUUIDs = true, includeState = true).sync()
            for (channel in channels) {
                val occupant = here.channels[channel]?.occupants?.firstOrNull { it.uuid == user }
                assertTrue(occupant?.state != null, "the state of the user is not visible on $channel yet")
            }
        }
        return joiner
    }

    /**
     * Removes the history of [channels] and waits until the service lists nothing, so that an exact read is exact. The history
     * is removed again after the test.
     */
    @JvmStatic
    fun resetHistory(
        client: PubNub,
        vararg channels: String,
    ) {
        for (channel in channels) {
            Retry.retryTransient(label = "delete history of $channel") { client.deleteMessages(listOf(channel)).sync() }
            Cleanup.record("history", channel)
        }
        Wait.settled("the history of ${channels.toList()} is empty") {
            val found = client.fetchMessages(channels.toList()).sync().channels
            assertTrue(found.values.all { it.isEmpty() }, "the history of ${channels.toList()} still lists messages")
        }
    }

    /** The timetokens of the stored messages of [channel], oldest first. */
    @JvmStatic
    fun storedTimetokens(
        client: PubNub,
        channel: String,
    ): List<Long?> = client.fetchMessages(listOf(channel)).sync().channels[channel].orEmpty().map { it.timetoken }

    /** The texts of the stored string messages of [channel] (the legacy history call, at most [count]), oldest first. */
    @JvmStatic
    @Suppress("DEPRECATION")
    fun historyTexts(
        client: PubNub,
        channel: String,
        count: Int,
    ): List<String> = client.history(channel, count = count).sync().messages.map { it.entry.asString }

    /** The action timetokens the service lists on [channel]. */
    @JvmStatic
    fun actionTimetokens(
        client: PubNub,
        channel: String,
    ): List<Long?> = client.getMessageActions(channel).sync().actions.map { it.actionTimetoken }

    /** The number of message actions the service lists on [channel]. */
    @JvmStatic
    fun actionCount(
        client: PubNub,
        channel: String,
    ): Int = client.getMessageActions(channel).sync().actions.size

    /** Waits until the history of [channel] lists an action on the message with [timetoken]. */
    @JvmStatic
    fun awaitActionInHistory(
        client: PubNub,
        channel: String,
        timetoken: Long,
    ) {
        Wait.settled("the history lists an action on the message $timetoken") {
            val items = client.fetchMessages(listOf(channel), includeMessageActions = true).sync().channels[channel].orEmpty()
            val item = items.firstOrNull { it.timetoken == timetoken }
            assertTrue(item?.actions?.isNotEmpty() == true, "the history does not list an action on the message yet")
        }
    }

    /** Publishes [message] to [channel] with storage and waits until the history lists it. */
    @JvmStatic
    fun publishStored(
        client: PubNub,
        channel: String,
        message: Any,
    ): PNPublishResult {
        val published = Kit.publishFrom(client, channel, message, true)
        Kit.storedItem(client, channel, published.timetoken)
        return published
    }

    /** Removes every message action of [channel] now and after the test (actions outlive the deleted message). */
    @JvmStatic
    fun clearActions(
        client: PubNub,
        channel: String,
    ) {
        removeActions(client, channel)
        Cleanup.cleanupAfterTest("remove the message actions of $channel") { removeActions(client, channel) }
    }

    private fun removeActions(
        client: PubNub,
        channel: String,
    ) {
        repeat(20) {
            val actions = Retry.retryTransient(label = "list actions of $channel") { client.getMessageActions(channel).sync().actions }
            if (actions.isEmpty()) {
                return
            }
            for (action in actions) {
                Retry.retryTransient(label = "remove action of $channel") {
                    client.removeMessageAction(channel, action.messageTimetoken, action.actionTimetoken!!).sync()
                }
            }
        }
    }

    /** Uploads the fixture as [fileName] to [channel] and deletes the file after the test. Returns the uploaded file. */
    @JvmStatic
    fun upload(
        client: PubNub,
        channel: String,
        fileName: String,
    ): PNBaseFile {
        val uploaded =
            Retry.retryTransient(label = "upload $fileName") {
                client.sendFile(channel, fileName, ByteArrayInputStream(fixtureBytes())).sync()
            }
        Cleanup.cleanupAfterTest("delete file $fileName of $channel") {
            client.deleteFile(channel, fileName, uploaded.file.id).sync()
        }
        return uploaded.file
    }

    /** The ids of the files the service lists on [channel]. */
    @JvmStatic
    fun fileIds(
        client: PubNub,
        channel: String,
    ): List<String> = client.listFiles(channel).sync().data.map { it.id }

    /** Reads a stream to its end and closes it. */
    @JvmStatic
    fun readAll(stream: java.io.InputStream): ByteArray = stream.use { it.readBytes() }

    /** Waits until the list of files of [channel] shows [fileId]. */
    @JvmStatic
    fun awaitListed(
        client: PubNub,
        channel: String,
        fileId: String,
    ) {
        Wait.settled("file $fileId is listed on $channel") {
            val ids = client.listFiles(channel).sync().data.map { it.id }
            assertTrue(fileId in ids, "the file is not listed on $channel yet")
        }
    }

    /** A file event on [channel] whose message text contains [contains]. Arm it before the action, call `await` after it. */
    @JvmStatic
    fun armFile(
        client: PubNub,
        channel: String,
        contains: String,
    ): Wait.Expectation =
        Wait.expectEvent(client, "file") { event ->
            event is PNFileEventResult && event.channel == channel && event.jsonMessage.toString().contains(contains)
        }

    /** Reads [url] with a plain GET (redirects are followed) and returns the status and the body. */
    @JvmStatic
    fun get(url: String): Pair<Int, ByteArray> {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.instanceFollowRedirects = true
        connection.connectTimeout = 15_000
        connection.readTimeout = 15_000
        return try {
            val status = connection.responseCode
            val stream =
                if (status < 400) {
                    connection.inputStream
                } else {
                    connection.errorStream
                }
            val body = stream?.use { it.readBytes() } ?: ByteArray(0)
            status to body
        } finally {
            connection.disconnect()
        }
    }

    /** Fails unless the file at [url] answers 200 with the bytes of the fixture. */
    @JvmStatic
    fun assertFixtureAt(url: String) {
        val (status, body) = get(url)
        assertEquals(200, status, "the file url did not answer 200")
        assertTrue(fixtureBytes().contentEquals(body), "the bytes behind the file url differ from the uploaded fixture")
    }
}
