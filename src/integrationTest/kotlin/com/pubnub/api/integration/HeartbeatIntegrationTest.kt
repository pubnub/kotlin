package com.pubnub.api.integration

import com.pubnub.api.CommonUtils.generatePayload
import com.pubnub.api.CommonUtils.randomChannel
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
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
            configuration.userId = UserId("observer_${System.currentTimeMillis()}")
        }

        pubnub.configuration.presenceTimeout = 20
        pubnub.configuration.heartbeatInterval = 4

        observer.addListener(object : SubscribeCallback() {

            override fun status(p: PubNub, pnStatus: PNStatus) {
                if (pnStatus.operation == PNOperationType.PNSubscribeOperation &&
                    pnStatus.affectedChannels.contains(expectedChannel)
                ) {
                    pubnub.subscribe(
                        channels = listOf(expectedChannel),
                        withPresence = true
                    )
                }
            }

            override fun presence(p: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.uuid.equals(pubnub.configuration.userId.value) &&
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
                                pubnub.setPresenceState(
                                    state = expectedStatePayload,
                                    channels = listOf(expectedChannel)
                                ).async { result, status ->
                                    assertFalse(status.error)
                                    assertEquals(expectedStatePayload, result!!.state)
                                    hits.incrementAndGet()
                                    stateSet.set(true)
                                }

                                Awaitility.await().atMost(Durations.FIVE_SECONDS)
                                    .untilTrue(stateSet)
                            } else {
                                assertEquals(expectedStatePayload, pnPresenceEventResult.state)
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
