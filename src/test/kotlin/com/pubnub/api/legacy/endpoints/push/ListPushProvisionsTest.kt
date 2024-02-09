package com.pubnub.api.legacy.endpoints.push

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.CommonUtils.assertPnException
import com.pubnub.api.CommonUtils.failTest
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.listen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

class ListPushProvisionsTest : BaseTest() {

    @Test
    fun testAppleSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("""["ch1", "ch2", "ch3"]"""))
        )

        val response = pubnub.auditPushChannelProvisions(
            deviceId = "niceDevice",
            pushType = PNPushType.APNS,
            topic = "irrelevant"
        ).sync()

        assertEquals(listOf("ch1", "ch2", "ch3"), response.channels)

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("apns", requests[0].queryParameter("type").firstValue())
        assertFalse(requests[0].queryParameter("environment").isPresent)
        assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testFirebaseSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("""["ch1", "ch2", "ch3"]"""))
        )

        val response = pubnub.auditPushChannelProvisions(
            deviceId = "niceDevice",
            pushType = PNPushType.FCM,
            topic = "irrelevant"
        ).sync()

        assertEquals(listOf("ch1", "ch2", "ch3"), response.channels)

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("gcm", requests[0].queryParameter("type").firstValue())
        assertFalse(requests[0].queryParameter("environment").isPresent)
        assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testMicrosoftSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("""["ch1", "ch2", "ch3"]"""))
        )

        val response = pubnub.auditPushChannelProvisions(
            deviceId = "niceDevice",
            pushType = PNPushType.MPNS,
            topic = "irrelevant"
        ).sync()

        assertEquals(listOf("ch1", "ch2", "ch3"), response.channels)

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("mpns", requests[0].queryParameter("type").firstValue())
        assertFalse(requests[0].queryParameter("environment").isPresent)
        assertFalse(requests[0].queryParameter("topic").isPresent)
    }

    @Test
    fun testApns2SuccessSync() {
        stubFor(
            get(urlPathEqualTo("/v2/push/sub-key/mySubscribeKey/devices-apns2/niceDevice"))
                .willReturn(aResponse().withBody("""["ch1", "ch2", "ch3"]"""))
        )

        val response = pubnub.auditPushChannelProvisions(
            deviceId = "niceDevice",
            pushType = PNPushType.APNS2,
            topic = "news"
        ).sync()

        assertEquals(listOf("ch1", "ch2", "ch3"), response.channels)

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("development", requests[0].queryParameter("environment").firstValue())
        assertEquals("news", requests[0].queryParameter("topic").firstValue())
    }

    @Test
    fun testApns2SuccessSync_Environment() {
        stubFor(
            get(urlPathEqualTo("/v2/push/sub-key/mySubscribeKey/devices-apns2/niceDevice"))
                .willReturn(aResponse().withBody("""["ch1", "ch2", "ch3"]"""))
        )

        val response = pubnub.auditPushChannelProvisions(
            deviceId = "niceDevice",
            pushType = PNPushType.APNS2,
            topic = "news",
            environment = PNPushEnvironment.PRODUCTION
        ).sync()

        assertEquals(listOf("ch1", "ch2", "ch3"), response.channels)

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("production", requests[0].queryParameter("environment").firstValue())
        assertEquals("news", requests[0].queryParameter("topic").firstValue())
    }

    @Test
    fun testEmptyArray() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[]"))
        )

        val response = pubnub.auditPushChannelProvisions(
            deviceId = "niceDevice",
            pushType = PNPushType.FCM
        ).sync()

        assertEquals(emptyList<String>(), response.channels)

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
    }

    @Test
    fun testEmptyBody() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody(""))
        )

        try {
            pubnub.auditPushChannelProvisions(
                deviceId = "niceDevice",
                pushType = PNPushType.FCM
            ).sync()
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

    @Test
    fun testMalformedResponseSync() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(ok().withBody("{"))
        )

        try {
            pubnub.auditPushChannelProvisions(
                deviceId = "niceDevice",
                pushType = PNPushType.FCM
            ).sync()
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

    @Test
    fun testMalformedResponseAsync() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(ok().withBody("{"))
        )

        val success = AtomicBoolean()
        pubnub.auditPushChannelProvisions(
            deviceId = "niceDevice",
            pushType = PNPushType.FCM
        ).async { result ->
            result.onFailure {
                assertPnException(PubNubError.PARSING_ERROR, it)
                success.set(true)
            }
        }
        success.listen()
    }

    @Test
    fun testIsAuthRequiredSuccess() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("""["ch1", "ch2", "ch3"]"""))
        )

        pubnub.configuration.authKey = "myKey"

        pubnub.auditPushChannelProvisions(
            deviceId = "niceDevice",
            pushType = PNPushType.APNS
        ).sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testOperationTypeSuccess() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("""["ch1", "ch2", "ch3"]"""))
        )

        val success = AtomicBoolean()

        pubnub.auditPushChannelProvisions(
            deviceId = "niceDevice",
            pushType = PNPushType.APNS
        ).async { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
//                assertTrue(status.affectedChannels.isEmpty()) //TODO should we hgave this
//                assertTrue(status.affectedChannelGroups.isEmpty())
                success.set(true)
            }
        }

        success.listen()
    }

    @Test
    fun testNullSubscribeKey() {
        stubFor(
            get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("""["ch1", "ch2", "ch3"]"""))
        )

        pubnub.configuration.subscribeKey = " "

        try {
            pubnub.auditPushChannelProvisions(
                deviceId = "niceDevice",
                pushType = PNPushType.APNS
            ).sync()
            failTest("Didn't throw SUBSCRIBE_KEY_MISSING")
        } catch (e: Exception) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testValidationNoTopic() {
        try {
            pubnub.auditPushChannelProvisions(
                deviceId = "niceDevice",
                pushType = PNPushType.APNS2
            ).sync()
            failTest("Should throw no topic")
        } catch (e: PubNubException) {
            assertPnException(PubNubError.PUSH_TOPIC_MISSING, e)
        }
    }

    @Test
    fun testValidationDefaultEnvironment() {
        stubFor(
            get(urlPathEqualTo("/v2/push/sub-key/mySubscribeKey/devices-apns2/niceDevice"))
                .willReturn(aResponse().withBody("""["ch1", "ch2", "ch3"]"""))
        )

        pubnub.auditPushChannelProvisions(
            deviceId = "niceDevice",
            pushType = PNPushType.APNS2,
            topic = UUID.randomUUID().toString()
        ).sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("development", requests[0].queryParameter("environment").firstValue())
    }
}
