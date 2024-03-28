package com.pubnub.api.legacy.endpoints.push

import com.github.tomakehurst.wiremock.client.WireMock
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.legacy.BaseTest
import com.pubnub.test.CommonUtils.assertPnException
import com.pubnub.test.CommonUtils.failTest
import com.pubnub.test.listen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class AddChannelsToPushTest : BaseTest() {
    @Test
    fun testAddAppleSuccessSync() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]")),
        )

        pubnub.addPushNotificationsOnChannels(
            deviceId = "niceDevice",
            pushType = PNPushType.APNS,
            channels = listOf("ch1", "ch2", "ch3"),
        ).sync()

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("ch1,ch2,ch3", requests[0].queryParameter("add").firstValue())
        assertEquals("apns", requests[0].queryParameter("type").firstValue())
        assertFalse(requests[0].queryParameter("environment").isPresent)
        assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testAddFirebaseSuccessSync() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]")),
        )

        pubnub.addPushNotificationsOnChannels(
            deviceId = "niceDevice",
            pushType = PNPushType.FCM,
            channels = listOf("ch1", "ch2", "ch3"),
        ).sync()

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("ch1,ch2,ch3", requests[0].queryParameter("add").firstValue())
        assertEquals("gcm", requests[0].queryParameter("type").firstValue())
        assertFalse(requests[0].queryParameter("environment").isPresent)
        assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testAddMicrosoftSuccessSync() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]")),
        )

        pubnub.addPushNotificationsOnChannels(
            deviceId = "niceDevice",
            pushType = PNPushType.MPNS,
            channels = listOf("ch1", "ch2", "ch3"),
        ).sync()

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("ch1,ch2,ch3", requests[0].queryParameter("add").firstValue())
        assertEquals("mpns", requests[0].queryParameter("type").firstValue())
        assertFalse(requests[0].queryParameter("environment").isPresent)
        assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testAddApns2SuccessSync() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v2/push/sub-key/mySubscribeKey/devices-apns2/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]")),
        )

        pubnub.addPushNotificationsOnChannels(
            deviceId = "niceDevice",
            pushType = PNPushType.APNS2,
            channels = listOf("ch1", "ch2", "ch3"),
            topic = "topic",
        ).sync()

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("ch1,ch2,ch3", requests[0].queryParameter("add").firstValue())
        assertEquals("development", requests[0].queryParameter("environment").firstValue())
        assertEquals("topic", requests[0].queryParameter("topic").firstValue())
        assertFalse(requests[0].queryParameter("type").isPresent)
    }

    @Test
    fun testIsAuthRequiredSuccessAdd() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]")),
        )

        config.authKey = "myKey"

        pubnub.addPushNotificationsOnChannels(
            deviceId = "niceDevice",
            pushType = PNPushType.FCM,
            channels = listOf("ch1", "ch2", "ch3"),
        ).sync()

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testOperationTypeSuccessAdd() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]")),
        )

        val success = AtomicBoolean()

        pubnub.addPushNotificationsOnChannels(
            deviceId = "niceDevice",
            pushType = PNPushType.FCM,
            channels = listOf("ch1", "ch2", "ch3"),
        ).async { result ->
            assertFalse(result.isFailure)
            success.set(true)
        }

        success.listen()
    }

    @Test
    fun testEmptySubscribeKeyAdd() {
        config.subscribeKey = ""

        try {
            pubnub.addPushNotificationsOnChannels(
                deviceId = "niceDevice",
                pushType = PNPushType.FCM,
                channels = listOf("ch1", "ch2", "ch3"),
            ).sync()
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testEmptyDeviceIdAdd() {
        try {
            pubnub.addPushNotificationsOnChannels(
                pushType = PNPushType.FCM,
                channels = listOf("ch1", "ch2", "ch3"),
                deviceId = " ",
            ).sync()
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.DEVICE_ID_MISSING, e)
        }
    }

    @Test
    fun testEmptyChannelsAdd() {
        try {
            pubnub.addPushNotificationsOnChannels(
                deviceId = "niceDevice",
                pushType = PNPushType.FCM,
                channels = emptyList(),
            ).sync()
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }
}
