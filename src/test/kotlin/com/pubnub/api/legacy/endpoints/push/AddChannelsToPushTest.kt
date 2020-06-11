package com.pubnub.api.legacy.endpoints.push

import com.github.tomakehurst.wiremock.client.WireMock
import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.legacy.BaseTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean

class AddChannelsToPushTest : BaseTest() {

    @Test
    fun testAddAppleSuccessSync() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.addPushNotificationsOnChannels().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.APNS
            channels = listOf("ch1", "ch2", "ch3")
        }.sync()!!

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("ch1,ch2,ch3", requests[0].queryParameter("add").firstValue())
        Assertions.assertEquals("apns", requests[0].queryParameter("type").firstValue())
        Assertions.assertFalse(requests[0].queryParameter("environment").isPresent)
        Assertions.assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testAddFirebaseSuccessSync() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.addPushNotificationsOnChannels().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.FCM
            channels = listOf("ch1", "ch2", "ch3")
        }.sync()!!

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("ch1,ch2,ch3", requests[0].queryParameter("add").firstValue())
        Assertions.assertEquals("gcm", requests[0].queryParameter("type").firstValue())
        Assertions.assertFalse(requests[0].queryParameter("environment").isPresent)
        Assertions.assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testAddMicrosoftSuccessSync() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.addPushNotificationsOnChannels().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.MPNS
            channels = listOf("ch1", "ch2", "ch3")
        }.sync()!!

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("ch1,ch2,ch3", requests[0].queryParameter("add").firstValue())
        Assertions.assertEquals("mpns", requests[0].queryParameter("type").firstValue())
        Assertions.assertFalse(requests[0].queryParameter("environment").isPresent)
        Assertions.assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testAddApns2SuccessSync() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v2/push/sub-key/mySubscribeKey/devices-apns2/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.addPushNotificationsOnChannels().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.APNS2
            channels = listOf("ch1", "ch2", "ch3")
            topic = "topic"
        }.sync()!!

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("ch1,ch2,ch3", requests[0].queryParameter("add").firstValue())
        Assertions.assertEquals("development", requests[0].queryParameter("environment").firstValue())
        Assertions.assertEquals("topic", requests[0].queryParameter("topic").firstValue())
        Assertions.assertFalse(requests[0].queryParameter("type").isPresent)
    }

    @Test
    fun testIsAuthRequiredSuccessAdd() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.configuration.authKey = "myKey"

        pubnub.addPushNotificationsOnChannels().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.FCM
            channels = listOf("ch1", "ch2", "ch3")
        }.sync()!!

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("myKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testOperationTypeSuccessAdd() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        val success = AtomicBoolean()

        pubnub.addPushNotificationsOnChannels().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.FCM
            channels = listOf("ch1", "ch2", "ch3")
        }.async { _, status ->
            Assertions.assertFalse(status.error)
            Assertions.assertEquals(PNOperationType.PNAddPushNotificationsOnChannelsOperation, status.operation)
            Assertions.assertEquals(PNStatusCategory.PNAcknowledgmentCategory, status.category)
            Assertions.assertEquals(status.affectedChannels, listOf("ch1", "ch2", "ch3"))
            Assertions.assertTrue(status.affectedChannelGroups.isEmpty())
            success.set(true)
        }

        success.listen()
    }


    @Test
    fun testEmptySubscribeKeyAdd() {
        pubnub.configuration.subscribeKey = ""

        try {
            pubnub.addPushNotificationsOnChannels().apply {
                deviceId = "niceDevice"
                pushType = PNPushType.FCM
                channels = listOf("ch1", "ch2", "ch3")
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testNullPushTypeAdd() {
        try {
            pubnub.addPushNotificationsOnChannels().apply {
                deviceId = "niceDevice"
                channels = listOf("ch1", "ch2", "ch3")
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.PUSH_TYPE_MISSING, e)
        }
    }

    @Test
    fun testNullDeviceIdAdd() {
        try {
            pubnub.addPushNotificationsOnChannels().apply {
                pushType = PNPushType.FCM
                channels = listOf("ch1", "ch2", "ch3")
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.DEVICE_ID_MISSING, e)
        }
    }

    @Test
    fun testEmptyDeviceIdAdd() {
        try {
            pubnub.addPushNotificationsOnChannels().apply {
                pushType = PNPushType.FCM
                channels = listOf("ch1", "ch2", "ch3")
                deviceId = " "
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.DEVICE_ID_MISSING, e)
        }
    }

    @Test
    fun testMissingChannelsAdd() {
        try {
            pubnub.addPushNotificationsOnChannels().apply {
                deviceId = "niceDevice"
                pushType = PNPushType.FCM
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testEmptyChannelsAdd() {
        try {
            pubnub.addPushNotificationsOnChannels().apply {
                deviceId = "niceDevice"
                pushType = PNPushType.FCM
                channels = emptyList()
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }
}