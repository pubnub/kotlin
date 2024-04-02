package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.test.CommonUtils.generatePayload
import com.pubnub.test.CommonUtils.randomChannel
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class HeartbeatIntegrationTest : BaseIntegrationTest() {
    lateinit var expectedChannel: String

    override fun onBefore() {
        expectedChannel = randomChannel()
    }

    /**
     * Please note this test doesn't actually test sending state with the Heartbeat REST call
     */
    fun testStateWithHeartbeat() {
        val hits = AtomicInteger()
        val expectedStatePayload = generatePayload()

        val observer =
            createPubNub {
                userId = UserId("observer_${System.currentTimeMillis()}")
            }

        val pubnub =
            createPubNub {
                presenceTimeout = 20
                heartbeatInterval = 4
            }

        observer.addListener(
            object : com.pubnub.api.callbacks.SubscribeCallback() {
                override fun status(
                    pubnub: PubNub,
                    status: PNStatus,
                ) {
                    if (status.category == PNStatusCategory.PNConnectedCategory &&
                        status.affectedChannels.contains(expectedChannel)
                    ) {
                        pubnub.subscribe(
                            channels = listOf(expectedChannel),
                            withPresence = true,
                        )
                    }
                }

                override fun presence(
                    pubnub: PubNub,
                    result: PNPresenceEventResult,
                ) {
                    if (result.uuid.equals(pubnub.configuration.userId.value) &&
                        result.channel.equals(expectedChannel)
                    ) {
                        when (result.event) {
                            "state-change" -> {
                                assertEquals(expectedStatePayload, result.state)
                                hits.incrementAndGet()
                                pubnub.disconnect()
                            }

                            "join" -> {
                                if (result.state == null) {
                                    hits.incrementAndGet()
                                    val stateSet = AtomicBoolean()
                                    pubnub.setPresenceState(
                                        state = expectedStatePayload,
                                        channels = listOf(expectedChannel),
                                    ).async { result ->
                                        assertFalse(result.isFailure)
                                        result.onSuccess {
                                            assertEquals(expectedStatePayload, it.state)
                                            hits.incrementAndGet()
                                            stateSet.set(true)
                                        }
                                    }

                                    Awaitility.await().atMost(Durations.FIVE_SECONDS)
                                        .untilTrue(stateSet)
                                } else {
                                    assertEquals(expectedStatePayload, result.state)
                                    hits.incrementAndGet()
                                }
                            }

                            "timeout", "leave" -> {
                                pubnub.reconnect()
                            }
                        }
                    }
                }
            },
        )

        observer.subscribe(
            channels = listOf(expectedChannel),
            withPresence = true,
        )

        Awaitility.await()
            .atMost(40, TimeUnit.SECONDS)
            .untilAtomic(hits, IsEqual.equalTo(4))
    }
}
