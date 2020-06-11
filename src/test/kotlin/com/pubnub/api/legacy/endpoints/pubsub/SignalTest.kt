package com.pubnub.api.legacy.endpoints.pubsub

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.pubnub.api.*
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import okhttp3.HttpUrl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class SignalTest : BaseTest() {

    @Test
    fun testSignalGetSuccessSync() {
        stubFor(
            get(urlMatching("/signal/myPublishKey/mySubscribeKey/0/coolChannel.*"))
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

        pubnub.signal().apply {
            channel = "coolChannel"
            message = payload
        }.sync()

        val requests = findAll(getRequestedFor(urlMatching("/signal.*")))
        assertEquals(1, requests.size)
        val request = requests[0]
        assertEquals("myUUID", request.queryParameter("uuid").firstValue())

        val httpUrl = HttpUrl.parse(request.absoluteUrl)
        var decodedSignalPayload: String? = null
        if (httpUrl != null) {
            decodedSignalPayload = httpUrl.pathSegments()[httpUrl.pathSize() - 1]
        }
        assertEquals(pubnub.mapper.toJson(payload), decodedSignalPayload)
    }

    @Test
    fun testSignalGetSuccessAsync() {
        stubFor(
            get(urlMatching("/signal/myPublishKey/mySubscribeKey/0/coolChannel.*"))
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

        pubnub.signal().apply {
            channel = "coolChannel"
            message = payload
        }.async { result, status ->
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

        pubnub.subscribe().apply {
            channels = listOf("coolChannel")
        }.execute()

        success.listen()
    }

    @Test
    fun testSignalFailNoChannel() {
        try {
            pubnub.signal().apply {
                message = UUID.randomUUID().toString()
            }.sync()!!
            throw RuntimeException()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testSignalFailBlankChannel() {
        try {
            pubnub.signal().apply {
                channel = " "
                message = UUID.randomUUID().toString()
            }.sync()!!
            throw RuntimeException()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testSignalFailNoMessage() {
        try {
            pubnub.signal().apply {
                channel = UUID.randomUUID().toString()
            }.sync()!!
            throw RuntimeException()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.MESSAGE_MISSING, e)
        }
    }

    @Test
    fun testSignalTelemetryParam() {
        stubFor(
            get(urlMatching("/signal/myPublishKey/mySubscribeKey/0/coolChannel.*")).willReturn(
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
        pubnub.signal().apply {
            channel = "coolChannel"
            message = UUID.randomUUID().toString()
        }.sync()!!

        pubnub.time()
            .sync()

        val requests = findAll(getRequestedFor(urlMatching("/time/0.*")))
        assertEquals(1, requests.size)
        val request = requests[0]
        assertTrue(request.queryParameter("l_sig").isPresent)
    }
}