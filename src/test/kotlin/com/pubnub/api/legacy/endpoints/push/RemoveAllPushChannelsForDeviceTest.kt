package com.pubnub.api.legacy.endpoints.push

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.assertPnException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.failTest
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.listen
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean

class RemoveAllPushChannelsForDeviceTest : BaseTest() {

    @Test
    fun testAppleSuccessSyncRemoveAll() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.removeAllPushNotificationsFromDeviceWithPushToken().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.APNS
        }.sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("apns", requests[0].queryParameter("type").firstValue())
        Assertions.assertFalse(requests[0].queryParameter("environment").isPresent)
        Assertions.assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testFirebaseSuccessSyncRemoveAll() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.removeAllPushNotificationsFromDeviceWithPushToken().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.FCM
        }.sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("gcm", requests[0].queryParameter("type").firstValue())
        Assertions.assertFalse(requests[0].queryParameter("environment").isPresent)
        Assertions.assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testMicrosoftSuccessSyncRemoveAll() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.removeAllPushNotificationsFromDeviceWithPushToken().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.MPNS
        }.sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("mpns", requests[0].queryParameter("type").firstValue())
        Assertions.assertFalse(requests[0].queryParameter("environment").isPresent)
        Assertions.assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testApns2SuccessSyncRemoveAll() {
        stubFor(
            get(urlPathEqualTo("/v2/push/sub-key/mySubscribeKey/devices-apns2/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.removeAllPushNotificationsFromDeviceWithPushToken().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.APNS2
            topic = "news"
        }.sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("development", requests[0].queryParameter("environment").firstValue())
        Assertions.assertEquals("news", requests[0].queryParameter("topic").firstValue())
        Assertions.assertFalse(requests[0].queryParameter("type").isPresent)
    }

    @Test
    fun testIsAuthRequiredSuccessRemoveAll() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        pubnub.configuration.authKey = "myKey"

        pubnub.removeAllPushNotificationsFromDeviceWithPushToken().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.FCM
        }.sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        Assertions.assertEquals(1, requests.size)
        Assertions.assertEquals("myKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testOperationTypeSuccessRemoveAll() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]"))
        )

        val success = AtomicBoolean()

        pubnub.removeAllPushNotificationsFromDeviceWithPushToken().apply {
            deviceId = "niceDevice"
            pushType = PNPushType.FCM
        }.async { _, status ->
            Assertions.assertFalse(status.error)
            Assertions.assertEquals(PNOperationType.PNRemoveAllPushNotificationsOperation, status.operation)
            Assertions.assertEquals(PNStatusCategory.PNAcknowledgmentCategory, status.category)
            Assertions.assertTrue(status.affectedChannels.isEmpty())
            Assertions.assertTrue(status.affectedChannelGroups.isEmpty())
            success.set(true)
        }

        success.listen()
    }

    @Test
    fun testEmptySubscribeKeyRemoveAll() {
        pubnub.configuration.subscribeKey = " "

        try {
            pubnub.removeAllPushNotificationsFromDeviceWithPushToken().apply {
                deviceId = "niceDevice"
                pushType = PNPushType.FCM
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testNullPushTypeRemoveAll() {
        try {
            pubnub.removeAllPushNotificationsFromDeviceWithPushToken().apply {
                deviceId = "niceDevice"
            }.sync()!!
        } catch (e: PubNubException) {
            assertPnException(PubNubError.PUSH_TYPE_MISSING, e)
        }
    }

    @Test
    fun testNullDeviceIdRemoveAll() {
        try {
            pubnub.removeAllPushNotificationsFromDeviceWithPushToken().apply {
                pushType = PNPushType.FCM
            }.sync()!!
        } catch (e: PubNubException) {
            assertPnException(PubNubError.DEVICE_ID_MISSING, e)
        }
    }

    @Test
    fun testEmptyDeviceIdRemoveAll() {
        try {
            pubnub.removeAllPushNotificationsFromDeviceWithPushToken().apply {
                pushType = PNPushType.FCM
                deviceId = " "
            }.sync()!!
        } catch (e: PubNubException) {
            assertPnException(PubNubError.DEVICE_ID_MISSING, e)
        }
    }
}
