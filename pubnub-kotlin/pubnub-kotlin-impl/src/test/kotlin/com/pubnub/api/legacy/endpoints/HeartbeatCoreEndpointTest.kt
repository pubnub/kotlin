package com.pubnub.api.legacy.endpoints

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.PubNubError
import com.pubnub.api.legacy.BaseTest
import com.pubnub.internal.endpoints.presence.HeartbeatEndpoint
import com.pubnub.test.CommonUtils.assertPnException
import com.pubnub.test.CommonUtils.failTest
import org.junit.Assert.assertEquals
import org.junit.Test

class HeartbeatCoreEndpointTest : BaseTest() {
    @Test
    fun testSuccessOneChannel() {
        config.presenceTimeout = 123
        // initPubNub()

        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/heartbeat"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "message": "OK",
                          "service": "Presence"
                        }
                        """.trimIndent(),
                    ),
                ),
        )

        HeartbeatEndpoint(pubnub, listOf("ch1")).sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)

        val request = requests[0]
        assertEquals("myUUID", request.queryParameter("uuid").firstValue())
        assertEquals("123", request.queryParameter("heartbeat").firstValue())
    }

    @Test
    fun testSuccessManyChannels() {
        config.presenceTimeout = 123
        // initPubNub()

        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1,ch2/heartbeat"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "message": "OK",
                          "service": "Presence"
                        }
                        """.trimIndent(),
                    ),
                ),
        )

        HeartbeatEndpoint(pubnub, listOf("ch1", "ch2")).sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)

        val request = requests[0]
        assertEquals("myUUID", request.queryParameter("uuid").firstValue())
        assertEquals("123", request.queryParameter("heartbeat").firstValue())
    }

    @Test
    fun testSuccessOneChannelGroup() {
        config.presenceTimeout = 123
        // initPubNub()
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/,/heartbeat"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "message": "OK",
                          "service": "Presence"
                        }
                        """.trimIndent(),
                    ),
                ),
        )

        HeartbeatEndpoint(pubnub, channelGroups = listOf("cg1")).sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)

        val request = requests[0]
        assertEquals("myUUID", request.queryParameter("uuid").firstValue())
        assertEquals("cg1", request.queryParameter("channel-group").firstValue())
        assertEquals("123", request.queryParameter("heartbeat").firstValue())
    }

    @Test
    fun testSuccessManyChannelGroups() {
        config.presenceTimeout = 123
        // initPubNub()

        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/,/heartbeat"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "message": "OK",
                          "service": "Presence"
                        }
                        """.trimIndent(),
                    ),
                ),
        )

        HeartbeatEndpoint(pubnub, channelGroups = listOf("cg1", "cg2")).sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)

        val request = requests[0]
        assertEquals("myUUID", request.queryParameter("uuid").firstValue())
        assertEquals("cg1,cg2", request.queryParameter("channel-group").firstValue())
        assertEquals("123", request.queryParameter("heartbeat").firstValue())
    }

    @Test
    fun testMissingChannelAndGroupSync() {
        config.presenceTimeout = 123
        // initPubNub()

        try {
            HeartbeatEndpoint(pubnub).sync()
            failTest()
        } catch (e: Exception) {
            assertPnException(
                PubNubError.CHANNEL_AND_GROUP_MISSING,
                e,
            )
        }
    }

    @Test
    fun testChannelListContainsOnlyEmptyStringSync() {
        config.presenceTimeout = 123

        try {
            HeartbeatEndpoint(pubnub, channels = listOf("", "channel1")).sync()
            failTest()
        } catch (e: Exception) {
            assertPnException(
                PubNubError.CHANNEL_AND_GROUP_CONTAINS_EMPTY_STRING,
                e,
            )
        }
    }

    @Test
    fun testChannelGroupListContainsOnlyEmptyStringSync() {
        config.presenceTimeout = 123

        try {
            HeartbeatEndpoint(pubnub, channelGroups = listOf("", "channelGroup1")).sync()
            failTest()
        } catch (e: Exception) {
            assertPnException(
                PubNubError.CHANNEL_AND_GROUP_CONTAINS_EMPTY_STRING,
                e,
            )
        }
    }

    @Test
    fun testIsAuthRequiredSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/heartbeat"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "status": 200,
                          "message": "OK",
                          "service": "Presence"
                        }
                        """.trimIndent(),
                    ),
                ),
        )

        config.authKey = "myKey"
        // initPubNub()

        HeartbeatEndpoint(pubnub, listOf("ch1")).sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testBlankSubKeySync() {
        config.presenceTimeout = 123
        config.subscribeKey = " "

        try {
            HeartbeatEndpoint(pubnub, listOf("ch1")).sync()
            failTest()
        } catch (e: Exception) {
            assertPnException(
                PubNubError.SUBSCRIBE_KEY_MISSING,
                e,
            )
        }
    }

    @Test
    fun testEmptySubKeySync() {
        config.presenceTimeout = 123
        config.subscribeKey = ""

        try {
            HeartbeatEndpoint(pubnub, listOf("ch1")).sync()
            failTest()
        } catch (e: Exception) {
            assertPnException(
                PubNubError.SUBSCRIBE_KEY_MISSING,
                e,
            )
        }
    }
}
