@file:Suppress("ktlint")

package com.pubnub.docs.mobilePush

import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.MessagingKit.APPLE_TOKEN
import com.pubnub.docs.support.MessagingKit.GOOGLE_TOKEN
import com.pubnub.docs.support.MessagingKit.awaitPush
import com.pubnub.docs.support.MessagingKit.seedPush
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Stdout
import com.pubnub.docs.support.Wait
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Live tests of the Kotlin docs page mobile-push (docs/sdks/kotlin/api-reference/mobile-push.md). One test per snippet. The
 * text between a start marker and `snippet.end` is the published sample, the arrangement is above it and the assertions
 * below it. The samples register device tokens, they do not send a push. The tokens of the docs (`googleDevice`,
 * `appleDevice`) are swapped for live hex tokens at the client call, because the service checks the shape of an APNs token.
 */
@ExtendWith(LiveExtension::class)
class MobilePushDocsTest {
    private fun clientWithPlaceholders(clients: LiveClients, recorder: Recorder) =
        recorder
            .placeholder("googleDevice", GOOGLE_TOKEN)
            .placeholder("appleDevice", APPLE_TOKEN)
            .wrap(clients.kotlinClient(OPEN, "myUserId"))

    @Test
    fun addDeviceToChannel(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        val notificationChannels = listOf("alerts", "news", "messages")
        val fcmToken = GOOGLE_TOKEN
        seedPush(reader, PNPushType.FCM, fcmToken)
        val out = Stdout.captureForTest()

        // snippet.addDeviceToChannel
        pubnub.addPushNotificationsOnChannels(
            pushType = PNPushType.FCM,
            channels = notificationChannels,
            deviceId = fcmToken
        ).async { result ->
            result.onSuccess { response: PNPushAddChannelResult ->
                println("SUCCESS: FCM device registered for push notifications on channels: $notificationChannels")
            }.onFailure { exception ->
                println("ERROR: Failed to register FCM device for push notifications")
                println("Error details: ${exception.message}")
            }
        }
        // snippet.end

        recorder.awaitOk("addPushNotificationsOnChannels")
        Wait.eventually("the sample printed the success line") {
            out.assertPrinted("SUCCESS: FCM device registered for push notifications on channels: [alerts, news, messages]")
        }
        awaitPush(reader, PNPushType.FCM, fcmToken, "alerts", "news", "messages")
    }

    @Test
    fun auditPushChannelProvisions(clients: LiveClients, recorder: Recorder) {
        val pubnub = clientWithPlaceholders(clients, recorder)
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        seedPush(reader, PNPushType.FCM, GOOGLE_TOKEN, "ch1", "ch2")
        seedPush(reader, PNPushType.APNS2, APPLE_TOKEN, "ch1", "ch3")

        // snippet.auditPushChannelProvisions
        // for FCM/GCM
        pubnub.auditPushChannelProvisions(
            pushType = PNPushType.FCM,
            deviceId = "googleDevice"
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }

        // for APNS2
        pubnub.auditPushChannelProvisions(
            pushType = PNPushType.APNS2,
            deviceId = "appleDevice",
            topic = "myapptopic",
            environment = PNPushEnvironment.DEVELOPMENT
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        // both calls finish on their own threads, so the two records are told apart by what they list
        val first = recorder.awaitOk("auditPushChannelProvisions", 1) as PNPushListProvisionsResult
        val second = recorder.awaitOk("auditPushChannelProvisions", 2) as PNPushListProvisionsResult
        assertEquals(setOf(listOf("ch1", "ch2"), listOf("ch1", "ch3")), setOf(first.channels.sorted(), second.channels.sorted()))
    }

    @Test
    fun removeAllPushNotificationsFromDeviceWithPushToken(clients: LiveClients, recorder: Recorder) {
        val pubnub = clientWithPlaceholders(clients, recorder)
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        seedPush(reader, PNPushType.FCM, GOOGLE_TOKEN, "ch1", "ch2")
        seedPush(reader, PNPushType.APNS2, APPLE_TOKEN, "ch1", "ch3")

        // snippet.removeAllPushNotificationsFromDeviceWithPushToken
        // for FCM/GCM
        pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
            deviceId = "googleDevice",
            pushType = PNPushType.FCM
        ).async { result -> }

        // for APNS2
        pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
            deviceId = "appleDevice",
            pushType = PNPushType.APNS2,
            topic = "myapptopic",
            environment = PNPushEnvironment.DEVELOPMENT
        ).async { result -> }
        // snippet.end

        recorder.awaitOk("removeAllPushNotificationsFromDeviceWithPushToken", 1)
        recorder.awaitOk("removeAllPushNotificationsFromDeviceWithPushToken", 2)
        awaitPush(reader, PNPushType.FCM, GOOGLE_TOKEN)
        awaitPush(reader, PNPushType.APNS2, APPLE_TOKEN)
    }

    @Test
    fun removePushNotificationsFromChannels(clients: LiveClients, recorder: Recorder) {
        val pubnub = clientWithPlaceholders(clients, recorder)
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        seedPush(reader, PNPushType.FCM, GOOGLE_TOKEN, "ch1", "ch2", "ch3", "ch4")
        seedPush(reader, PNPushType.APNS2, APPLE_TOKEN, "ch1", "ch2", "ch3", "ch4")

        // snippet.removePushNotificationsFromChannels
        // for FCM/GCM
        pubnub.removePushNotificationsFromChannels(
            deviceId = "googleDevice",
            channels = listOf("ch1", "ch2", "ch3"),
            pushType = PNPushType.FCM
        ).async { result -> }

        // for APNS2
        pubnub.removePushNotificationsFromChannels(
            deviceId = "appleDevice",
            channels = listOf("ch1", "ch2", "ch3"),
            pushType = PNPushType.APNS2,
            topic = "myapptopic",
            environment = PNPushEnvironment.DEVELOPMENT
        ).async { result -> }
        // snippet.end

        recorder.awaitOk("removePushNotificationsFromChannels", 1)
        recorder.awaitOk("removePushNotificationsFromChannels", 2)
        awaitPush(reader, PNPushType.FCM, GOOGLE_TOKEN, "ch4")
        awaitPush(reader, PNPushType.APNS2, APPLE_TOKEN, "ch4")
    }

    private companion object {
        const val OPEN = "open"
    }
}
