@file:Suppress("ktlint")

package com.pubnub.docs.miscellaneous

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.crypto.CryptoModule
import com.pubnub.api.crypto.cryptor.Cryptor
import com.pubnub.api.crypto.data.EncryptedData
import com.pubnub.api.crypto.data.EncryptedStreamData
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.push.payload.PushPayloadHelper
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.utils.Instant
import com.pubnub.api.utils.TimetokenUtil
import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.Kit.armMessage
import com.pubnub.docs.support.Kit.armPresence
import com.pubnub.docs.support.Kit.assertNothingArrives
import com.pubnub.docs.support.Kit.assertTimetoken
import com.pubnub.docs.support.Kit.awaitNoOccupants
import com.pubnub.docs.support.Kit.createGroup
import com.pubnub.docs.support.Kit.probe
import com.pubnub.docs.support.Kit.subscribeAndConnect
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Retry
import com.pubnub.docs.support.Stdout
import com.pubnub.docs.support.Wait
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.InputStream

/**
 * Live tests of the Kotlin docs page misc (docs/sdks/kotlin/api-reference/misc.md). One test per snippet. The text between a
 * start marker and `snippet.end` is the published sample, the arrangement is above it and the assertions below it.
 */
@ExtendWith(LiveExtension::class)
class MiscellaneousDocsTest {
    private val OPEN = "open"

    // ---------------------------------------------------------------------------------------------------------------
    // Create push payload
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun createPushPayloadMain() {
        val out = Stdout.captureForTest()

        com.pubnub.docs.miscellaneous.samples.main()

        out.assertPrinted("PubNub PushPayloadHelper Example")
        out.assertPrinted("- FCM (Android): true")
        out.assertPrinted("- APNS (iOS): true")
        out.assertPrinted("- Common fields: [message, type]")
        out.assertPrinted("- Contains FCM payload: true")
        out.assertPrinted("- Contains APNS payload: true")
        // message, meeting_id, type and action_required
        out.assertPrinted("- Common fields count: 4")
        assertEquals(0, out.lines("Exception").size)
    }

    @Test
    fun createPushPayloadMethod(clients: LiveClients) {
        val receiver = clients.kotlinClient(OPEN, "docs-config-receiver")
        val arrived = armMessage(receiver, "foo", "pn_fcm")
        subscribeAndConnect(receiver, listOf("foo"))
        Cleanup.record("history", "foo")

        // snippet.createPushPayloadMethod
        val pushPayloadHelper = PushPayloadHelper()

        val fcmPayload = PushPayloadHelper.FCMPayloadV2()
        val apnsPayload = PushPayloadHelper.APNSPayload()

        pushPayloadHelper.fcmPayloadV2 = fcmPayload
        pushPayloadHelper.apnsPayload = apnsPayload
        // snippet.end

        // the docs: build() returns a Map that goes straight into publish as the message
        // (the empty parts of the sample build to nothing, so the parts get content before the build)
        fcmPayload.notification = PushPayloadHelper.FCMPayloadV2.Notification().apply { title = "Title" }
        apnsPayload.aps = PushPayloadHelper.APNSPayload.APS().apply { alert = "Alert" }
        val payload = pushPayloadHelper.build()
        assertTrue(payload.containsKey("pn_fcm"), "the payload has no pn_fcm part")
        assertTrue(payload.containsKey("pn_apns"), "the payload has no pn_apns part")
        val published = clients.kotlinClient(OPEN, "docs-config-sender").publish("foo", payload).sync()
        val received = arrived.await() as PNMessageResult
        assertEquals(published.timetoken, received.timetoken)
        assertTrue(received.message.asJsonObject.has("pn_fcm"))
        assertTrue(received.message.asJsonObject.has("pn_apns"))
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Destroy, subscribed names, disconnect, reconnect
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun destroy(clients: LiveClients, recorder: Recorder) {
        val watcher = clients.kotlinClient(OPEN, "docs-config-receiver")
        awaitNoOccupants(watcher, "docs-misc-destroy")
        val joined = armPresence(watcher, "join", "docs-config-sender", "docs-misc-destroy")
        subscribeAndConnect(watcher, listOf("docs-misc-destroy"), presence = true)
        val client = clients.kotlinClient(OPEN, "docs-config-sender")
        subscribeAndConnect(client, listOf("docs-misc-destroy"))
        joined.await(Wait.PRESENCE_EVENT_TIMEOUT_MS)
        val text = probe("after-destroy")
        val seenByClient = armMessage(client, "docs-misc-destroy", text)
        val seenByWatcher = armMessage(watcher, "docs-misc-destroy", text)
        val pubNub = recorder.wrap(client)

        // snippet.destroy
        pubNub.destroy()
        // snippet.end

        // destroy frees the threads: the client is done and hears nothing, a watcher proves the service delivers
        clients.kotlinClient(OPEN, "docs-config-writer").publish("docs-misc-destroy", text).sync()
        seenByWatcher.await()
        assertNothingArrives(seenByClient, "a destroyed client received a message")
    }

    @Test
    fun getSubscribedChannelGroups(clients: LiveClients) {
        val pubNub = clients.kotlinClient(OPEN, "docs-config-sender")
        createGroup(pubNub, "docs-misc-group", "docs-misc-ch1")
        subscribeAndConnect(pubNub, groups = listOf("docs-misc-group"))

        // snippet.getSubscribedChannelGroups
        val subscribedChannelGroups = pubNub.getSubscribedChannelGroups()
        // snippet.end

        assertEquals(listOf("docs-misc-group"), subscribedChannelGroups)
    }

    @Test
    fun getSubscribedChannels(clients: LiveClients) {
        val pubNub = clients.kotlinClient(OPEN, "docs-config-sender")
        subscribeAndConnect(pubNub, listOf("docs-misc-ch1", "docs-misc-ch2"))

        // snippet.getSubscribedChannels
        val subscribedChannels = pubNub.getSubscribedChannels()
        // snippet.end

        assertEquals(setOf("docs-misc-ch1", "docs-misc-ch2"), subscribedChannels.toSet())
        assertEquals(2, subscribedChannels.size)
    }

    @Test
    fun disconnect(clients: LiveClients) {
        val pubNub = clients.kotlinClient(OPEN, "docs-config-sender")
        val publisher = clients.kotlinClient(OPEN, "docs-config-receiver")
        val control = clients.kotlinClient(OPEN, "docs-misc-control")
        val before = probe("before-disconnect")
        val after = probe("after-disconnect")
        val seenBefore = armMessage(pubNub, "docs-misc-ch1", before)
        val seenAfter = armMessage(pubNub, "docs-misc-ch1", after)
        val controlSeesAfter = armMessage(control, "docs-misc-ch1", after)
        subscribeAndConnect(pubNub, listOf("docs-misc-ch1"))
        subscribeAndConnect(control, listOf("docs-misc-ch1"))
        Cleanup.record("history", "docs-misc-ch1")
        publisher.publish("docs-misc-ch1", before).sync()
        seenBefore.await()

        // snippet.disconnect
        pubNub.disconnect()
        // snippet.end

        // a control client proves the service delivers while the disconnected client hears nothing
        publisher.publish("docs-misc-ch1", after).sync()
        controlSeesAfter.await()
        assertNothingArrives(seenAfter, "a disconnected client received a message")
        // the client keeps its subscriptions, only the connection is stopped
        assertEquals(listOf("docs-misc-ch1"), pubNub.getSubscribedChannels())
    }

    @Test
    fun reconnect(clients: LiveClients) {
        val pubNub = clients.kotlinClient(OPEN, "docs-config-sender")
        val publisher = clients.kotlinClient(OPEN, "docs-config-receiver")
        val whileAway = probe("while-away")
        val seenWhileAway = armMessage(pubNub, "docs-misc-ch1", whileAway)
        subscribeAndConnect(pubNub, listOf("docs-misc-ch1"))
        Cleanup.record("history", "docs-misc-ch1")
        pubNub.disconnect()

        // snippet.reconnect
        pubNub.reconnect()
        // or
        val timetoken = 17276954606232118L // Example timetoken received in publish/signal response
        pubNub.reconnect(timetoken)
        // snippet.end

        // the subscribe loop runs again and takes a new message
        val later = probe("after-reconnect")
        val seenLater = armMessage(pubNub, "docs-misc-ch1", later)
        val published = publisher.publish("docs-misc-ch1", later).sync()
        val received = seenLater.await() as PNMessageResult
        assertEquals(published.timetoken, received.timetoken)
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Timetoken conversions
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun timetokenToInstant() {
        val out = Stdout.captureForTest()

        // snippet.timetokenToInstant
        val timetoken: Long = 17276954606232118
        val instant: Instant = TimetokenUtil.timetokenToInstant(timetoken)
        val localDateTimeInUTC = instant.toLocalDateTime(TimeZone.UTC)

        println("PubNub timetoken: $timetoken")
        println("Current date: ${localDateTimeInUTC.date}")
        println("Current time: ${localDateTimeInUTC.time}")
        // snippet.end

        // the docs: 2024-09-30 11:24:20.623211800
        out.assertPrinted("PubNub timetoken: 17276954606232118")
        out.assertPrinted("Current date: 2024-09-30")
        out.assertPrinted("Current time: 11:24:20.623211800")
        assertEquals(1727695460623L, instant.toEpochMilliseconds())
        assertEquals(17276954606232118L, TimetokenUtil.instantToTimetoken(instant))
    }

    @Test
    fun instantToTimetoken() {
        val out = Stdout.captureForTest()

        // snippet.instantToTimetoken
        val localDateTime = LocalDateTime(
            year = 2024,
            month = 9,
            day = 30,
            hour = 12,
            minute = 12,
            second = 44,
            nanosecond = 123456789
        )

        val zone = TimeZone.currentSystemDefault()
        val instant: Instant = TimetokenUtil.localDateTimeToInstant(localDateTime, zone)
        val timetoken = TimetokenUtil.instantToTimetoken(instant)

        println("Current date: ${localDateTime.date}")
        println("Current time: ${localDateTime.time}")
        println("PubNub timetoken: $timetoken")
        // snippet.end

        out.assertPrinted("Current date: 2024-09-30")
        out.assertPrinted("Current time: 12:12:44.123456789")
        out.assertPrinted("PubNub timetoken: $timetoken")
        // the sample converts in the zone of the machine, so the timetoken is the one of that zone
        val utc = TimetokenUtil.instantToTimetoken(TimetokenUtil.localDateTimeToInstant(localDateTime, TimeZone.UTC))
        assertEquals(17276983641234567L, utc)
        assertEquals(utc, TimetokenUtil.localDateTimeToTimetoken(localDateTime, TimeZone.UTC))
        assertEquals(timetoken, TimetokenUtil.localDateTimeToTimetoken(localDateTime, zone))
        assertEquals(17, timetoken.toString().length)
    }

    @Test
    fun unixToTimetoken() {
        val out = Stdout.captureForTest()

        // snippet.unixToTimetoken
        val unixTime = 1727866935316
        val timetoken: Long = TimetokenUtil.unixToTimetoken(unixTime)
        val instant: Instant = TimetokenUtil.timetokenToInstant(timetoken)
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)

        println("Current date: ${localDateTime.date}")
        println("Current time: ${localDateTime.time}")
        println("PubNub timetoken: $timetoken")
        // snippet.end

        // the docs: 2024-10-02 11:02:15.316. The input is in milliseconds
        out.assertPrinted("Current date: 2024-10-02")
        out.assertPrinted("Current time: 11:02:15.316")
        out.assertPrinted("PubNub timetoken: 17278669353160000")
        assertEquals(17278669353160000L, timetoken)
    }

    @Test
    fun timetokenToUnix() {
        val out = Stdout.captureForTest()

        // snippet.timetokenToUnix
        val timetoken = 17276954606232118
        val unixTime = TimetokenUtil.timetokenToUnix(timetoken)
        val instant = Instant.fromEpochMilliseconds(unixTime)
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)

        println("Current date: ${localDateTime.date}")
        println("Current time: ${localDateTime.time}")
        println("PubNub timetoken: $timetoken")
        // snippet.end

        // the docs: 2024-09-30 11:24:20.623
        out.assertPrinted("Current date: 2024-09-30")
        out.assertPrinted("Current time: 11:24:20.623")
        out.assertPrinted("PubNub timetoken: 17276954606232118")
        assertEquals(1727695460623L, unixTime)
    }
}
