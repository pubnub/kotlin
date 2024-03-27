package com.pubnub.api.legacy.endpoints.channel_groups

import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.matching
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.PubNubError
import com.pubnub.api.legacy.BaseTest
import com.pubnub.test.CommonUtils.assertPnException
import com.pubnub.test.CommonUtils.emptyJson
import org.awaitility.Awaitility
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class AddChannelBaseChannelGroupCoreEndpointTest : BaseTest() {
    @Test
    fun testSyncSuccess() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .withQueryParam("add", matching("ch1,ch2"))
                .willReturn(emptyJson()),
        )

        pubnub.addChannelsToChannelGroup(
            channelGroup = "groupA",
            channels = listOf("ch1", "ch2"),
        ).sync()
    }

    @Test
    fun testSyncGroupIsEmpty() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(emptyJson()),
        )

        try {
            pubnub.addChannelsToChannelGroup(
                channelGroup = "",
                channels = listOf("ch1", "ch2"),
            ).sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.GROUP_MISSING, e)
        }
    }

    @Test
    fun testIsSubRequiredSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .withQueryParam("add", matching("ch1,ch2"))
                .willReturn(emptyJson()),
        )

        config.subscribeKey = " "
        // initPubNub()

        try {
            pubnub.addChannelsToChannelGroup(
                channelGroup = "groupA",
                channels = listOf("ch1", "ch2"),
            ).sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testIsAuthRequiredSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .withQueryParam("add", matching("ch1,ch2"))
                .willReturn(emptyJson()),
        )
        config.authKey = "myKey"
        // initPubNub()

        pubnub.addChannelsToChannelGroup(
            channelGroup = "groupA",
            channels = listOf("ch1", "ch2"),
        ).sync()

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myKey", requests[0].queryParameter("auth").firstValue())
    }

    @Test
    fun testOperationTypeSuccessAsync() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .withQueryParam("add", matching("ch1,ch2"))
                .willReturn(emptyJson()),
        )

        val atomic = AtomicInteger(0)

        pubnub.addChannelsToChannelGroup(
            channelGroup = "groupA",
            channels = listOf("ch1", "ch2"),
        ).async { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
                atomic.incrementAndGet()
            }
        }

        Awaitility.await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAtomic(atomic, IsEqual.equalTo(1))
    }

    @Test
    fun testErrorBodyForbidden() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .withQueryParam("add", matching("ch1,ch2"))
                .willReturn(emptyJson().withStatus(403)),
        )

        val atomic = AtomicInteger(0)

        pubnub.addChannelsToChannelGroup(
            channelGroup = "groupA",
            channels = listOf("ch1", "ch2"),
        ).async { result ->
            assertTrue(result.isFailure)
            result.onFailure {
                assertEquals(403, it.statusCode)
                atomic.incrementAndGet()
            }
        }

        Awaitility.await().atMost(15, TimeUnit.SECONDS).untilAtomic(atomic, IsEqual.equalTo(1))
    }
}
