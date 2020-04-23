package com.pubnub.api.endpoints.channel_groups

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import org.awaitility.Awaitility
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class AllChannelsChannelGroupEndpointTest : BaseTest() {

    @Test
    fun testSyncSuccess() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "message": "OK",
                          "payload": {
                            "channels": [
                              "a",
                              "b"
                            ]
                          },
                          "service": "ChannelGroups"
                        }
                    """.trimIndent()
                    )
                )
        )

        val response = pubnub.listChannelsForChannelGroup().apply {
            channelGroup = "groupA"
        }.sync()!!

        assertThat(response.channels, Matchers.contains("a", "b"))
    }

    @Test
    fun testSyncMissingGroup() {
        try {
            pubnub.listChannelsForChannelGroup().apply {

            }.sync()!!
        } catch (e: Exception) {
            assertPnException(PubNubError.GROUP_MISSING, e)
        }
    }

    @Test
    fun testSyncEmptyGroup() {
        try {
            pubnub.listChannelsForChannelGroup().apply {
                channelGroup = " "
            }.sync()!!
        } catch (e: Exception) {
            assertPnException(PubNubError.GROUP_MISSING, e)
        }
    }

    @Test
    fun testIsAuthRequiredSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                              "status": 200,
                              "message": "OK",
                              "payload": {
                                "channels": [
                                  "a",
                                  "b"
                                ]
                              },
                              "service": "ChannelGroups"
                            }
                        """.trimIndent()
                    )
                )
        )
        pubnub.configuration.authKey = "myKey"

        pubnub.listChannelsForChannelGroup().apply {
            channelGroup = "groupA"
        }.sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testOperationTypeSuccessAsync() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                              "status": 200,
                              "message": "OK",
                              "payload": {
                                "channels": [
                                  "a",
                                  "b"
                                ]
                              },
                              "service": "ChannelGroups"
                            }
                        """.trimIndent()
                    )
                )
        )

        val atomic = AtomicInteger(0)

        pubnub.listChannelsForChannelGroup().apply {
            channelGroup = "groupA"
        }.async { result, status ->
            println(status)
            assertFalse(status.error)
            assertEquals(PNOperationType.PNChannelsForGroupOperation, status.operation)
            assertEquals(PNStatusCategory.PNAcknowledgmentCategory, status.category)
            assertTrue(status.affectedChannels.isEmpty())
            assertEquals(listOf("groupA"), status.affectedChannelGroups)
            assertEquals(listOf("a", "b"), result!!.channels)
            atomic.incrementAndGet()
        }

        Awaitility.await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAtomic(atomic, IsEqual.equalTo(1))
    }

    @Test
    fun testIsSubRequiredSuccessSync() {
        pubnub.configuration.subscribeKey = " "
        try {
            pubnub.listChannelsForChannelGroup().apply {
                channelGroup = "groupA"
            }.sync()!!
            throw RuntimeException()
        } catch (e: PubNubException) {
            e.printStackTrace()
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testTelemetryParam() {
        val success = AtomicBoolean()

        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                              "status": 200,
                              "message": "OK",
                              "payload": {
                                "channels": [
                                  "a",
                                  "b"
                                ]
                              },
                              "service": "ChannelGroups"
                            }
                        """.trimIndent()
                    )
                )
        )

        stubFor(
            get(urlMatching("/time/0.*"))
                .willReturn(aResponse().withBody("[1000]"))
        )

        lateinit var telemetryParamName: String

        pubnub.listChannelsForChannelGroup().apply {
            channelGroup = "groupA"
        }.async { _, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNChannelsForGroupOperation, status.operation)
            telemetryParamName = "l_${status.operation.queryParam}"
            assertEquals("l_cg", telemetryParamName)
            success.set(true)
        }

        success.listen()

        pubnub.time().async { _, status ->
            assertFalse(status.error)
            assertNotNull(status.param(telemetryParamName))
            success.set(true)
        }

        success.listen()
    }


}