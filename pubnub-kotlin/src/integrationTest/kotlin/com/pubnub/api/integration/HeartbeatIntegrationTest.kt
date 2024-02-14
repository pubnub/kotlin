package com.pubnub.api.integration

import com.pubnub.api.CommonUtils.generatePayload
import com.pubnub.api.CommonUtils.randomChannel
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.v2.callbacks.onSuccess
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
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
    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun testStateWithHeartbeat(enableEE: Boolean) {
        val hits = AtomicInteger()
        val expectedStatePayload = generatePayload()

        val observer = createPubNub().apply {
            configuration.userId = UserId("observer_${System.currentTimeMillis()}")
        }

        val pubnub = createPubNub(
            getBasicPnConfiguration().apply {
                presenceTimeout = 20
                heartbeatInterval = 4
            }
        )

        observer.addListener(object : com.pubnub.api.callbacks.SubscribeCallback() {

            override fun status(p: PubNub, pnStatus: PNStatus) {
                if (pnStatus is PNStatus.Connected &&
                    pnStatus.channels.contains(expectedChannel)
                ) {
                    pubnub.subscribe(
                        channels = listOf(expectedChannel),
                        withPresence = true
                    )
                }
            }

            override fun presence(p: PubNub, event: PNPresenceEventResult) {
                if (event.uuid.equals(pubnub.configuration.userId.value) &&
                    event.channel.equals(expectedChannel)
                ) {
                    when (event.event) {
                        "state-change" -> {
                            assertEquals(expectedStatePayload, event.state)
                            hits.incrementAndGet()
                            pubnub.disconnect()
                        }
                        "join" -> {
                            if (event.state == null) {
                                hits.incrementAndGet()
                                val stateSet = AtomicBoolean()
                                pubnub.setPresenceState(
                                    state = expectedStatePayload,
                                    channels = listOf(expectedChannel)
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
                                assertEquals(expectedStatePayload, event.state)
                                hits.incrementAndGet()
                            }
                        }
                        "timeout", "leave" -> {
                            pubnub.reconnect()
                        }
                    }
                }
            }
        })

        observer.subscribe(
            channels = listOf(expectedChannel),
            withPresence = true
        )

        Awaitility.await()
            .atMost(40, TimeUnit.SECONDS)
            .untilAtomic(hits, IsEqual.equalTo(4))
    }
}
