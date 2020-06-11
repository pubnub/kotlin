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

class RemoveChannelsFromPushTest : BaseTest() {

    @Test
    fun testRemoveAppleSuccessSync() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.removePushNotificationsFromChannels().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.APNS
            channels = listOf("chr1", "chr2", "chr3")
        }.sync()!!

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("apns", requests[0].queryParameter("type").firstValue())
        Assertions.assertEquals("chr1,chr2,chr3", requests[0].queryParameter("remove").firstValue())
        Assertions.assertFalse(requests[0].queryParameter("environment").isPresent)
        Assertions.assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testRemoveFirebaseSuccessSync() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.removePushNotificationsFromChannels().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.FCM
            channels = listOf("chr1", "chr2", "chr3")
        }.sync()!!

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("gcm", requests[0].queryParameter("type").firstValue())
        Assertions.assertEquals("chr1,chr2,chr3", requests[0].queryParameter("remove").firstValue())
        Assertions.assertFalse(requests[0].queryParameter("environment").isPresent)
        Assertions.assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testRemoveMicrosoftSuccessSync() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.removePushNotificationsFromChannels().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.MPNS
            channels = listOf("chr1", "chr2", "chr3")
        }.sync()!!

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("mpns", requests[0].queryParameter("type").firstValue())
        Assertions.assertEquals("chr1,chr2,chr3", requests[0].queryParameter("remove").firstValue())
        Assertions.assertFalse(requests[0].queryParameter("environment").isPresent)
        Assertions.assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testRemoveApns2SuccessSync() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v2/push/sub-key/mySubscribeKey/devices-apns2/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.removePushNotificationsFromChannels().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.APNS2
            channels = listOf("chr1", "chr2", "chr3")
            topic = "news"
        }.sync()!!

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("chr1,chr2,chr3", requests[0].queryParameter("remove").firstValue())
        Assertions.assertEquals("development", requests[0].queryParameter("environment").firstValue())
        Assertions.assertEquals("news", requests[0].queryParameter("topic").firstValue())
        Assertions.assertFalse(requests[0].queryParameter("type").isPresent)
    }

    @Test
    fun testIsAuthRequiredSuccessRemove() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.configuration.authKey = "myKey"

        pubnub.removePushNotificationsFromChannels().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.FCM
            channels = listOf("chr1", "chr2", "chr3")
            topic = "news"
        }.sync()!!

        val requests = WireMock.findAll(WireMock.getRequestedFor(WireMock.urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("myKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testOperationTypeSuccessRemove() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(WireMock.aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        val success = AtomicBoolean()

        pubnub.removePushNotificationsFromChannels().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.FCM
            channels = listOf("chr1", "chr2", "chr3")
        }.async { _, status ->
            Assertions.assertFalse(status.error)
            Assertions.assertEquals(PNOperationType.PNRemovePushNotificationsFromChannelsOperation, status.operation)
            Assertions.assertEquals(PNStatusCategory.PNAcknowledgmentCategory, status.category)
            Assertions.assertEquals(status.affectedChannels, listOf("chr1", "chr2", "chr3"))
            Assertions.assertTrue(status.affectedChannelGroups.isEmpty())
            success.set(true)
        }

        success.listen()
    }

    @Test
    fun testEmptySubscribeKeyRemove() {
        pubnub.configuration.subscribeKey = " "

        try {
            pubnub.removePushNotificationsFromChannels().apply {
                deviceId = "niceDevice"
                pushType = PNPushType.FCM
                channels = listOf("chr1", "chr2", "chr3")
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testNullPushType() {
        try {
            pubnub.removePushNotificationsFromChannels().apply {
                deviceId = "niceDevice"
                channels = listOf("chr1", "chr2", "chr3")
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.PUSH_TYPE_MISSING, e)
        }
    }

    @Test
    fun testNullDeviceId() {
        try {
            pubnub.removePushNotificationsFromChannels().apply {
                pushType = PNPushType.FCM
                channels = listOf("chr1", "chr2", "chr3")
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.DEVICE_ID_MISSING, e)
        }
    }

    @Test
    fun testEmptyDeviceId() {
        try {
            pubnub.removePushNotificationsFromChannels().apply {
                pushType = PNPushType.FCM
                deviceId = " "
                channels = listOf("chr1", "chr2", "chr3")
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.DEVICE_ID_MISSING, e)
        }
    }

    @Test
    fun testMissingChannels() {
        try {
            pubnub.removePushNotificationsFromChannels().apply {
                pushType = PNPushType.FCM
                deviceId = "niceDevice"
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testEmptyChannels() {
        try {
            pubnub.removePushNotificationsFromChannels().apply {
                pushType = PNPushType.FCM
                deviceId = "niceDevice"
                channels = emptyList()
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

}