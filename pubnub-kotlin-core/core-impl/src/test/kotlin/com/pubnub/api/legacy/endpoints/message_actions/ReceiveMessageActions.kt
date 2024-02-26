package com.pubnub.api.legacy.endpoints.message_actions

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.BasePubNub
import com.pubnub.api.CommonUtils.failTest
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.listen
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.internal.callbacks.SubscribeCallback
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class ReceiveMessageActions : BaseTest() {
    @Test
    fun testReceiveMessageAction() {
        stubFor(
            get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/coolChannel/0"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                         "t": {
                          "t": "1000",
                          "r": 12
                         },
                         "m": [
                          {
                           "a": "1",
                           "f": 0,
                           "e": 3,
                           "i": "client-1639ed91",
                           "p": {
                            "t": "1000",
                            "r": 12
                           },
                           "k": "mySubscribeKey",
                           "c": "coolChannel",
                           "d": {
                            "source": "actions",
                            "version": "1.0",
                            "data": {
                             "messageTimetoken": "500",
                             "type": "reaction",
                             "value": "smiley",
                             "actionTimetoken": "600"
                            },
                            "event": "added"
                           }
                          }
                         ]
                        }
                        """.trimIndent(),
                    ),
                ),
        )

        val success = AtomicBoolean()

        pubnubBase.addListener(
            object : SubscribeCallback {
                override fun status(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnStatus: PNStatus,
                ) {
                }

                override fun message(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnMessageResult: PNMessageResult,
                ) {
                    failTest()
                }

                override fun presence(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnPresenceEventResult: PNPresenceEventResult,
                ) {
                    failTest()
                }

                override fun signal(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnSignalResult: PNSignalResult,
                ) {
                    failTest()
                }

                override fun messageAction(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnMessageActionResult: PNMessageActionResult,
                ) {
                    assertEquals(pnMessageActionResult.channel, "coolChannel")
                    assertEquals(pnMessageActionResult.messageAction.messageTimetoken, 500L)
                    assertEquals(pnMessageActionResult.messageAction.uuid, "client-1639ed91")
                    assertEquals(pnMessageActionResult.messageAction.actionTimetoken, 600L)
                    assertEquals(pnMessageActionResult.messageAction.type, "reaction")
                    assertEquals(pnMessageActionResult.messageAction.value, "smiley")
                    success.set(true)
                }
            },
        )

        pubnub.subscribe(
            channels = listOf("coolChannel"),
        )

        success.listen()
    }

    @Test
    fun testReceiveMessageActionMulti() {
        stubFor(
            get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/coolChannel/0"))
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                         "t": {
                          "t": "1000",
                          "r": 12
                         },
                         "m": [
                          {
                           "a": "1",
                           "f": 0,
                           "e": 3,
                           "i": "client-1639ed91",
                           "p": {
                            "t": "1000",
                            "r": 12
                           },
                           "k": "mySubscribeKey",
                           "c": "coolChannel",
                           "d": {
                            "source": "actions",
                            "version": "1.0",
                            "data": {
                             "messageTimetoken": "500",
                             "type": "reaction",
                             "value": "smiley",
                             "actionTimetoken": "600"
                            },
                            "event": "added"
                           }
                          },
                          {
                           "a": "1",
                           "f": 0,
                           "e": 3,
                           "i": "client-1639ed91",
                           "p": {
                            "t": "1000",
                            "r": 12
                           },
                           "k": "mySubscribeKey",
                           "c": "coolChannel",
                           "d": {
                            "source": "actions",
                            "version": "1.0",
                            "data": {
                             "messageTimetoken": "500",
                             "type": "reaction",
                             "value": "grinning",
                             "actionTimetoken": "699"
                            },
                            "event": "added"
                           }
                          }
                         ]
                        }
                        """.trimIndent(),
                    ),
                ),
        )

        val count = AtomicInteger()
        val success = AtomicBoolean()

        pubnubBase.addListener(
            object : SubscribeCallback {
                override fun status(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnStatus: PNStatus,
                ) {
                }

                override fun message(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnMessageResult: PNMessageResult,
                ) {
                    failTest()
                    pnMessageResult.message
                }

                override fun presence(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnPresenceEventResult: PNPresenceEventResult,
                ) {
                    failTest()
                }

                override fun signal(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnSignalResult: PNSignalResult,
                ) {
                    failTest()
                }

                override fun messageAction(
                    pubnub: BasePubNub<*, *, *, *, *, *, *, *>,
                    pnMessageActionResult: PNMessageActionResult,
                ) {
                    count.incrementAndGet()
                    if (count.get() == 2) {
                        success.set(true)
                    }
                }
            },
        )

        pubnub.subscribe(
            channels = listOf("coolChannel"),
        )

        success.listen()
    }
}
