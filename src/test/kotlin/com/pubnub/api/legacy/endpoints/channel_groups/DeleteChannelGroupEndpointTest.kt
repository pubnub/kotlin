package com.pubnub.api.legacy.endpoints.channel_groups

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.any
import com.github.tomakehurst.wiremock.client.WireMock.anyUrl
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.noContent
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.assertPnException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.failTest
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.listen
import com.pubnub.api.param
import org.awaitility.Awaitility
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class DeleteChannelGroupEndpointTest : BaseTest() {

    @Test
    fun testSyncSuccess() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA/remove"))
                .willReturn(
                    aResponse().withBody(
                        """{"status":200,"message":"OK","payload":{},"service":"ChannelGroups"}"""
                    )
                )
        )

        pubnub.deleteChannelGroup().apply {
            channelGroup = "groupA"
        }.sync()!!
    }

    @Test
    fun testSyncMissingGroup() {
        try {
            pubnub.deleteChannelGroup().apply {
            }.sync()!!
        } catch (e: PubNubException) {
            assertPnException(PubNubError.GROUP_MISSING, e)
        }
    }

    @Test
    fun testSyncEmptyGroup() {
        stubFor(any(anyUrl()).willReturn(aResponse()))

        try {
            pubnub.deleteChannelGroup().apply {
                channelGroup = " "
            }.sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.GROUP_MISSING, e)
        }
    }

    @Test
    fun testIsAuthRequiredSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA/remove"))
                .willReturn(
                    aResponse().withBody(
                        """{"status": 200, "message": "OK", "payload": {}, "service": "ChannelGroups"}"""
                    )
                )
        )

        pubnub.configuration.authKey = "myKey"

        pubnub.deleteChannelGroup().apply {
            channelGroup = "groupA"
        }.sync()!!

        val requests =
            findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testMalformedResponse() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA/remove"))
                .willReturn(noContent())
        )

        pubnub.deleteChannelGroup().apply {
            channelGroup = "groupA"
        }.sync()!!
    }

    @Test
    fun testOperationTypeSuccessAsync() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA/remove"))
                .willReturn(
                    aResponse().withBody(
                        """{"status": 200, "message": "OK", "payload": {}, "service": "ChannelGroups"}"""
                    )
                )
        )

        val atomic = AtomicInteger(0)

        pubnub.deleteChannelGroup().apply {
            channelGroup = "groupA"
        }.async { _, status ->
            Assertions.assertFalse(status.error)
            assertEquals(PNOperationType.PNRemoveGroupOperation, status.operation)
            assertEquals(PNStatusCategory.PNAcknowledgmentCategory, status.category)
            Assertions.assertTrue(status.affectedChannels == emptyList<String>())
            Assertions.assertTrue(status.affectedChannelGroups == listOf("groupA"))
            atomic.incrementAndGet()
        }

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAtomic(atomic, IsEqual.equalTo(1))
    }

    @Test
    fun testTelemetryParam() {
        val success = AtomicBoolean()

        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA/remove"))
                .willReturn(
                    aResponse().withBody(
                        """{"status": 200, "message": "OK", "payload": {}, "service": "ChannelGroups"}"""
                    )
                )
        )

        stubFor(
            get(urlMatching("/time/0.*"))
                .willReturn(aResponse().withBody("[1000]"))
        )

        lateinit var telemetryParamName: String

        pubnub.deleteChannelGroup().apply {
            channelGroup = "groupA"
        }.async { _, status ->
            Assertions.assertFalse(status.error)
            assertEquals(PNOperationType.PNRemoveGroupOperation, status.operation)
            telemetryParamName = "l_${status.operation.queryParam}"
            assertEquals("l_cg", telemetryParamName)
            success.set(true)
        }

        success.listen()

        pubnub.time().async { _, status ->
            Assertions.assertFalse(status.error)
            Assertions.assertNotNull(status.param(telemetryParamName))
            success.set(true)
        }

        success.listen()
    }
}
