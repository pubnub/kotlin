package com.pubnub.api.legacy.endpoints.channel_groups

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.matching
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.CommonUtils.assertPnException
import com.pubnub.api.CommonUtils.emptyJson
import com.pubnub.api.PubNubError
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.listen
import org.awaitility.Awaitility
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class AddChannelChannelGroupEndpointTest : BaseTest() {

    @Test
    fun testSyncSuccess() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .withQueryParam("add", matching("ch1,ch2"))
                .willReturn(emptyJson())
        )

        pubnub.addChannelsToChannelGroup(
            channelGroup = "groupA",
            channels = listOf("ch1", "ch2")
        ).sync()
    }

    @Test
    fun testSyncGroupIsEmpty() {
        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(emptyJson())
        )

        try {
            pubnub.addChannelsToChannelGroup(
                channelGroup = "",
                channels = listOf("ch1", "ch2")
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
                .willReturn(emptyJson())
        )

        pubnub.configuration.subscribeKey = " "

        try {
            pubnub.addChannelsToChannelGroup(
                channelGroup = "groupA",
                channels = listOf("ch1", "ch2")
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
                .willReturn(emptyJson())
        )
        pubnub.configuration.authKey = "myKey"

        pubnub.addChannelsToChannelGroup(
            channelGroup = "groupA",
            channels = listOf("ch1", "ch2")
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
                .willReturn(emptyJson())
        )

        val atomic = AtomicInteger(0)

        pubnub.addChannelsToChannelGroup(
            channelGroup = "groupA",
            channels = listOf("ch1", "ch2")
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
                .willReturn(emptyJson().withStatus(403))
        )

        val atomic = AtomicInteger(0)

        pubnub.addChannelsToChannelGroup(
            channelGroup = "groupA",
            channels = listOf("ch1", "ch2")
        ).async { result ->
            assertTrue(result.isFailure)
            result.onFailure {
//                assertEquals(PNStatusCategory.PNAccessDeniedCategory, status.category) // TODO how to check forbidden
//                atomic.incrementAndGet()
            }
        }

        Awaitility.await().atMost(15, TimeUnit.SECONDS).untilAtomic(atomic, IsEqual.equalTo(1))
    }

    @Test
    fun testTelemetryParam() {
        val success = AtomicBoolean()

        stubFor(
            get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(emptyJson())
        )

        stubFor(
            get(urlMatching("/time/0.*"))
                .willReturn(aResponse().withBody("[1000]"))
        )

        lateinit var telemetryParamName: String

        pubnub.addChannelsToChannelGroup(
            channelGroup = "groupA",
            channels = listOf("ch1", "ch2")
        ).async { result ->
            assertFalse(result.isFailure)
//            telemetryParamName = "l_${status.operation.queryParam}" //TODO this is no longer available
//            assertEquals("l_cg", telemetryParamName)
//            success.set(true)
        }

        success.listen()

        pubnub.time().async { result ->
            assertFalse(result.isFailure)
//            assertNotNull(status.param(telemetryParamName)) //TODO no longer available
//            success.set(true)
        }

        success.listen()
    }
}
