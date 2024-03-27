package com.pubnub.api.legacy.endpoints.channel_groups

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.legacy.BaseTest
import org.awaitility.Awaitility
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class RemoveChannelBaseChannelGroupCoreEndpointTest : BaseTest() {
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
                         "payload": {},
                         "service": "ChannelGroups"
                        }
                        """.trimIndent(),
                    ),
                ),
        )

        pubnub.removeChannelsFromChannelGroup(
            channelGroup = "groupA",
            channels = arrayListOf("ch1", "ch2"),
        ).sync()
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
                         "payload": {},
                         "service": "ChannelGroups"
                        }
                        """.trimIndent(),
                    ),
                ),
        )

        config.authKey = "myKey"

        pubnub.removeChannelsFromChannelGroup(
            channelGroup = "groupA",
            channels = arrayListOf("ch1", "ch2"),
        ).sync()

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
                         "payload": {},
                         "service": "ChannelGroups"
                        }
                        """.trimIndent(),
                    ),
                ),
        )

        val atomic = AtomicInteger(0)

        pubnub.removeChannelsFromChannelGroup(
            channelGroup = "groupA",
            channels = arrayListOf("ch1", "ch2"),
        ).async { result ->
            assertFalse(result.isFailure)
            atomic.incrementAndGet()
        }

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAtomic(atomic, IsEqual.equalTo(1))
    }
}
