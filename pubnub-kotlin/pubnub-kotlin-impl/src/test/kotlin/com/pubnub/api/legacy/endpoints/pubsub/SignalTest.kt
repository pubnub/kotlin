package com.pubnub.api.legacy.endpoints.pubsub

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.test.CommonUtils.assertPnException
import com.pubnub.test.listen
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.util.UUID
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
                            """.trimIndent(),
                        ),
                ),
        )

        val payload = mapOf("text" to "hello")

        pubnub.signal(
            channel = "coolChannel",
            message = payload,
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
                            """.trimIndent(),
                        ),
                ),
        )

        val payload = UUID.randomUUID().toString()

        val success = AtomicBoolean()

        pubnub.signal(
            channel = "coolChannel",
            message = payload,
        ).async { result ->
            assertFalse(result.isFailure)
            result.onSuccess {
                assertEquals("1000", it.timetoken.toString())
                success.set(true)
            }
        }

        success.listen()
    }

    @Test
    fun testSignalSuccessReceive() {
        val channelName = "coolChannel"
        val message = "hello"
        val uuid = "uuid"
        val customMessageType = "myCustomMessageType"
        stubFor(
            get(urlMatching("/v2/subscribe/mySubscribeKey/$channelName/0.*"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                          "m": [
                            {
                              "c": "$channelName",
                              "f": "0",
                              "i": "$uuid",
                              "d": "$message",
                              "e": 1,
                              "p": {
                                "t": 1000,
                                "r": 1
                              },
                              "k": "mySubscribeKey",
                              "b": "$channelName",
                              "cmt" : "$customMessageType"
                            }
                          ],
                          "t": {
                            "r": "56",
                            "t": 1000
                          }
                        }
                        """.trimIndent(),
                    ),
                ),
        )
        stubForHeartbeatWhenHeartbeatIntervalIs0ThusPresenceEEDoesNotWork(setOf(channelName))
        val success = AtomicBoolean()

        pubnub.addListener(
            object : SubscribeCallback() {
                override fun signal(
                    pubnub: PubNub,
                    pnSignalResult: PNSignalResult,
                ) {
                    assertEquals(channelName, pnSignalResult.channel)
                    assertEquals(message, pnSignalResult.message.asString)
                    assertEquals(uuid, pnSignalResult.publisher)
                    assertEquals(customMessageType, pnSignalResult.customMessageType)
                    success.set(true)
                }

                override fun status(
                    pubnub: PubNub,
                    pnStatus: PNStatus,
                ) {}
            },
        )

        pubnub.subscribe(
            channels = listOf(channelName),
        )

        success.listen()
    }

    @Test
    fun testSignalFailBlankChannel() {
        try {
            pubnub.signal(
                channel = " ",
                message = UUID.randomUUID().toString(),
                customMessageType = "myType"
            ).sync()
            throw RuntimeException()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }
}
