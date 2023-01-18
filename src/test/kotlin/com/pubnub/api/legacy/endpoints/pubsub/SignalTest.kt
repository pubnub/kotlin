package com.pubnub.api.legacy.endpoints.pubsub

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.pubnub.api.CommonUtils.assertPnException
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.SpaceId
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.listen
import com.pubnub.api.models.consumer.MessageType
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

class SignalTest : BaseTest() {

    private val channel = "coolChannel"

    @Test
    fun testSignalGetSuccessSync() {
        stubFor(
            get(urlMatching("/signal/myPublishKey/mySubscribeKey/0/$channel.*"))
                .willReturn(
                    aResponse()
                        .withBody(
                            """
                            [
                              1,
                              "Sent",
                              "1000"
                            ]
                            """.trimIndent()
                        )
                )
        )

        val payload = mapOf("text" to "hello")

        pubnub.signal(
            channel = "coolChannel",
            message = payload
        ).sync()

        val requests = findAll(getRequestedFor(urlMatching("/signal.*")))
        assertEquals(1, requests.size)
        val request = requests[0]
        assertEquals("myUUID", request.queryParameter("uuid").firstValue())

        val httpUrl = request.absoluteUrl.toHttpUrlOrNull()
        var decodedSignalPayload: String? = null
        if (httpUrl != null) {
            decodedSignalPayload = httpUrl.pathSegments[httpUrl.pathSize - 1]
        }
        assertEquals(pubnub.mapper.toJson(payload), decodedSignalPayload)
    }

    @Test
    fun testSignalGetSuccessAsync() {
        stubFor(
            get(urlMatching("/signal/myPublishKey/mySubscribeKey/0/$channel.*"))
                .willReturn(
                    aResponse()
                        .withBody(
                            """
                            [
                              1,
                              "Sent",
                              "1000"
                            ]
                            """.trimIndent()
                        )
                )
        )

        val payload = UUID.randomUUID().toString()

        val success = AtomicBoolean()

        pubnub.signal(
            channel = "coolChannel",
            message = payload
        ).async { result, status ->
            result!!
            assertFalse(status.error)
            assertEquals(PNOperationType.PNSignalOperation, status.operation)
            assertEquals("1000", result.timetoken.toString())
            success.set(true)
        }

        success.listen()
    }

    @Test
    fun testSignalSuccessReceive() {
        stubFor(
            get(urlMatching("/v2/subscribe/mySubscribeKey/coolChannel/0.*"))
                .willReturn(
                    aResponse().withBody(
                        """
                            {
                              "m": [
                                {
                                  "c": "coolChannel",
                                  "f": "0",
                                  "i": "uuid",
                                  "d": "hello",
                                  "e": 1,
                                  "p": {
                                    "t": 1000,
                                    "r": 1
                                  },
                                  "k": "mySubscribeKey",
                                  "b": "coolChannel"
                                }
                              ],
                              "t": {
                                "r": "56",
                                "t": 1000
                              }
                            }
                        """.trimIndent()
                    )
                )
        )

        val success = AtomicBoolean()

        pubnub.addListener(object : SubscribeCallback() {
            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
                assertEquals("coolChannel", pnSignalResult.channel)
                assertEquals("hello", pnSignalResult.message.asString)
                assertEquals("uuid", pnSignalResult.publisher)
                success.set(true)
            }

            override fun status(pubnub: PubNub, pnStatus: PNStatus) {}
            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {}
            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {}
            override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {}
        })

        pubnub.subscribe(
            channels = listOf("coolChannel")
        )

        success.listen()
    }

    @Test
    fun testSignalFailBlankChannel() {
        try {
            pubnub.signal(
                channel = " ",
                message = UUID.randomUUID().toString()
            ).sync()!!
            throw RuntimeException()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testSignalTelemetryParam() {
        stubFor(
            get(urlMatching("/signal/myPublishKey/mySubscribeKey/0/$channel.*")).willReturn(
                aResponse().withBody(
                    """
                    [
                      1,
                      "Sent",
                      "1000"
                    ]
                    """.trimIndent()
                )
            )
        )
        stubFor(
            get(urlMatching("/time/0.*"))
                .willReturn(aResponse().withBody("[1000]"))
        )
        pubnub.signal(
            channel = "coolChannel",
            message = UUID.randomUUID().toString()
        ).sync()!!

        pubnub.time()
            .sync()

        val requests = findAll(getRequestedFor(urlMatching("/time/0.*")))
        assertEquals(1, requests.size)
        val request = requests[0]
        assertTrue(request.queryParameter("l_sig").isPresent)
    }

    @Test
    fun testSpaceIdQueryParamIsPassed() {
        val spaceIdValue = "thisIsSpaceId"

        stubFor(
            get(WireMock.urlPathEqualTo("/signal/myPublishKey/mySubscribeKey/0/$channel/0/%22hi%22"))
                .willReturn(aResponse().withBody("""[1,"Sent","15883272000000000"]"""))
        )

        pubnub.signal(
            channel = channel,
            message = "hi",
            spaceId = SpaceId(spaceIdValue)
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        MatcherAssert.assertThat(
            requests[0].queryParams.map { (k, v) -> k to v.firstValue() }.toMap(),
            Matchers.hasEntry(Publish.SPACE_ID_QUERY_PARAM, spaceIdValue)
        )
    }

    @Test
    fun testMissingSpaceIdQueryParamIsNotSet() {
        stubFor(
            get(WireMock.urlPathEqualTo("/signal/myPublishKey/mySubscribeKey/0/$channel/0/%22hi%22"))
                .willReturn(aResponse().withBody("""[1,"Sent","15883272000000000"]"""))
        )

        pubnub.signal(
            channel = channel,
            message = "hi"
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        MatcherAssert.assertThat(requests[0].queryParams, Matchers.not(Matchers.hasKey(Publish.SPACE_ID_QUERY_PARAM)))
    }

    @Test
    fun testMessageTypeQueryParamIsPassedInPublish() {
        val messageTypeValue = "thisIsSpaceId"

        stubFor(
            get(WireMock.urlPathEqualTo("/signal/myPublishKey/mySubscribeKey/0/$channel/0/%22hi%22"))
                .willReturn(aResponse().withBody("""[1,"Sent","15883272000000000"]"""))
        )

        pubnub.signal(
            channel = channel,
            message = "hi",
            messageType = MessageType(messageTypeValue)
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        MatcherAssert.assertThat(
            requests[0].queryParams.map { (k, v) -> k to v.firstValue() }.toMap(),
            Matchers.hasEntry(Publish.MESSAGE_TYPE_QUERY_PARAM, messageTypeValue)
        )
    }

    @Test
    fun testMissingMessageTypeQueryParamIsNotSet() {
        stubFor(
            get(WireMock.urlPathEqualTo("/signal/myPublishKey/mySubscribeKey/0/$channel/0/%22hi%22"))
                .willReturn(aResponse().withBody("""[1,"Sent","15883272000000000"]"""))
        )

        pubnub.signal(
            channel = channel,
            message = "hi"
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        MatcherAssert.assertThat(
            requests[0].queryParams,
            Matchers.not(Matchers.hasKey(Publish.MESSAGE_TYPE_QUERY_PARAM))
        )
    }
}
