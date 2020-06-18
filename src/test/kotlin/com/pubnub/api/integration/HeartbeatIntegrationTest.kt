package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.randomChannel
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.util.Arrays
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class HeartbeatIntegrationTest : BaseIntegrationTest() {

    lateinit var expectedChannel: String

    override fun onBefore() {
        expectedChannel = randomChannel()
    }

    @Test
    fun testStateWithHeartbeat() {
        val hits = AtomicInteger()
        val expectedStatePayload = generatePayload()

        val observer = createPubNub().apply {
            configuration.uuid = "observer_${System.currentTimeMillis()}"
        }

        pubnub.configuration.presenceTimeout = 20
        pubnub.configuration.heartbeatInterval = 4

        observer.addListener(object : SubscribeCallback() {

            override fun status(p: PubNub, pnStatus: PNStatus) {
                if (pnStatus.operation == PNOperationType.PNSubscribeOperation &&
                    pnStatus.affectedChannels.contains(expectedChannel)
                ) {
                    pubnub.subscribe().apply {
                        channels = listOf(expectedChannel)
                        withPresence = true
                    }.execute()
                }
            }

            override fun presence(p: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.uuid.equals(pubnub.configuration.uuid) &&
                    pnPresenceEventResult.channel.equals(expectedChannel)
                ) {
                    when (pnPresenceEventResult.event) {
                        "state-change" -> {
                            assertEquals(expectedStatePayload, pnPresenceEventResult.state)
                            hits.incrementAndGet()
                            pubnub.disconnect()
                        }
                        "join" -> {
                            if (pnPresenceEventResult.state == null) {
                                hits.incrementAndGet()
                                val stateSet = AtomicBoolean()
                                pubnub.setPresenceState().apply {
                                    state = expectedStatePayload
                                    channels = Arrays.asList(expectedChannel)
                                }.async { result, status ->
                                    assertFalse(status.error)
                                    assertEquals(expectedStatePayload, result!!.state)
                                    hits.incrementAndGet()
                                    stateSet.set(true)
                                }

                                Awaitility.await().atMost(Durations.FIVE_SECONDS).untilTrue(stateSet)
                            } else {
                                assertEquals(expectedStatePayload, pnPresenceEventResult.state)
                                hits.incrementAndGet()
                            }
                        }
                        "timeout" -> {
                            pubnub.reconnect()
                        }
                    }
                }
            }
        })

        observer.subscribe().apply {
            channels = listOf(expectedChannel)
            withPresence = true
        }.execute()

        Awaitility.await()
            .atMost(40, TimeUnit.SECONDS)
            .untilAtomic(hits, IsEqual.equalTo(4))
    }
}
