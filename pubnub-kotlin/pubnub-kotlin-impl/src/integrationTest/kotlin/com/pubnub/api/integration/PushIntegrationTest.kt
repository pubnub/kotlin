package com.pubnub.api.integration

import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale
import java.util.UUID

class PushIntegrationTest : BaseIntegrationTest() {
    lateinit var expectedChannels: List<String>
    lateinit var expectedDeviceId: String
    lateinit var expectedTopic: String

    override fun onBefore() {
        expectedChannels =
            generateSequence { UUID.randomUUID().toString().substring(0, 8).uppercase(Locale.getDefault()) }.take(3)
                .toList()
        expectedDeviceId =
            generateSequence { (0..9).random() }.take(70).toList().shuffled().joinToString(separator = "")
        expectedTopic = UUID.randomUUID().toString()
    }

    @Test
    fun testEnumNames() {
        assertEquals("apns", PNPushType.APNS.toParamString())
        assertEquals("fcm", PNPushType.FCM.toParamString())
        assertEquals("apns2", PNPushType.APNS2.toParamString())
    }

    @Test
    fun testCycle() {
        PNPushType.values().forEach {
            runPushOperations(it)
        }
    }

    private fun runPushOperations(pushType: PNPushType) {
        println("Push type '$pushType'")
        println("Channels $expectedChannels'")
        println("Topic $expectedTopic'")
        println("GeneratedToken '${expectedDeviceId.length}': $expectedDeviceId")

        pubnub.addPushNotificationsOnChannels(
            channels = expectedChannels,
            pushType = pushType,
            topic = expectedTopic,
            deviceId = expectedDeviceId,
        ).sync()

        wait()

        pubnub.auditPushChannelProvisions(
            deviceId = expectedDeviceId,
            pushType = pushType,
            topic = expectedTopic,
            environment = PNPushEnvironment.DEVELOPMENT,
        ).sync().run {
            assertTrue(channels.containsAll(expectedChannels))
        }

        wait()

        pubnub.removePushNotificationsFromChannels(
            pushType = pushType,
            environment = PNPushEnvironment.DEVELOPMENT,
            deviceId = expectedDeviceId,
            topic = expectedTopic,
            channels = listOf(expectedChannels[0]),
        ).sync()

        wait()

        pubnub.auditPushChannelProvisions(
            deviceId = expectedDeviceId,
            pushType = pushType,
            topic = expectedTopic,
            environment = PNPushEnvironment.DEVELOPMENT,
        ).sync().apply {
            assertFalse(channels.isEmpty())
            assertFalse(channels.contains(expectedChannels[0]))
        }

        wait()

        pubnub.removeAllPushNotificationsFromDeviceWithPushToken(
            pushType = pushType,
            environment = PNPushEnvironment.DEVELOPMENT,
            deviceId = expectedDeviceId,
            topic = expectedTopic,
        ).sync()

        wait()

        pubnub.auditPushChannelProvisions(
            deviceId = expectedDeviceId,
            pushType = pushType,
            topic = expectedTopic,
            environment = PNPushEnvironment.DEVELOPMENT,
        ).sync().apply {
            assertTrue(channels.isEmpty())
            assertFalse(channels.containsAll(expectedChannels))
        }
    }
}
