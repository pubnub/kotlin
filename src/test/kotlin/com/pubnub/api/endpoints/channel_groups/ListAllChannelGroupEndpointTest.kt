package com.pubnub.api.endpoints.channel_groups

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import org.awaitility.Awaitility
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class ListAllChannelGroupEndpointTest : BaseTest() {

    @Test
    fun testSyncSuccess() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group"))
                .willReturn(
                    aResponse().withBody(
                        "{\"status\": 200, \"message\": \"OK\", \"payload\": {\"groups\": " +
                                "[\"a\",\"b\"]}, \"service\": \"ChannelGroups\"}"
                    )
                )
        )

        val response = pubnub.listAllChannelGroups().sync()!!
        assertThat(response.groups, Matchers.contains("a", "b"))
    }

    @Test
    fun testNullPayload() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group"))
                .willReturn(
                    aResponse().withBody(
                        "{\"status\": 200, \"message\": \"OK\", \"service\": " +
                                "\"ChannelGroups\"}"
                    )
                )
        )

        try {
            pubnub.listAllChannelGroups().sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

    @Test
    fun testNullBody() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group"))
                .willReturn(aResponse())
        )

        try {
            pubnub.listAllChannelGroups().sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.PARSING_ERROR, e)
        }
    }

    @Test
    fun testIsAuthRequiredSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group"))
                .willReturn(
                    aResponse().withBody(
                        "{\"status\": 200, \"message\": \"OK\", \"payload\": {\"groups\": " +
                                "[\"a\",\"b\"]}, \"service\": \"ChannelGroups\"}"
                    )
                )
        )

        pubnub.configuration.authKey = "myKey"

        pubnub.listAllChannelGroups().sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testOperationTypeSuccessAsync() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group"))
                .willReturn(
                    aResponse().withBody(
                        "{\"status\": 200, \"message\": \"OK\", \"payload\": {\"groups\": " +
                                "[\"a\",\"b\"]}, \"service\": \"ChannelGroups\"}"
                    )
                )
        )

        val atomic = AtomicInteger(0)

        pubnub.listAllChannelGroups()
            .async { _, status ->
                Assertions.assertFalse(status.error)
                assertEquals(PNOperationType.PNChannelGroupsOperation, status.operation)
                assertEquals(PNStatusCategory.PNAcknowledgmentCategory, status.category)
                Assertions.assertTrue(status.affectedChannels.isEmpty())
                Assertions.assertTrue(status.affectedChannelGroups.isEmpty())
                atomic.incrementAndGet()
            }

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
            .untilAtomic(atomic, IsEqual.equalTo(1))
    }
}