package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.TestEventSink
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.milliseconds

class WaitEffectTest {
    private val heartbeatInterval = 1.milliseconds
    private val presenceEventSink = TestEventSink<PresenceEvent>()

    @Test
    fun `should deliver TimesUp event when WaitEffect finishes successfully`() {
        // given
        val waitEffect = WaitEffect(heartbeatInterval, presenceEventSink)

        // when
        waitEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(java.time.Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(PresenceEvent.TimesUp), presenceEventSink.events)
            }
    }

    @Test
    fun `should not deliver TimesUp event when cancelled on time`() {
        // given
        val waitEffect = WaitEffect(20.milliseconds, presenceEventSink)

        // when
        waitEffect.runEffect()
        waitEffect.cancel()

        assertEquals(emptyList<PresenceEvent>(), presenceEventSink.events)

        // then
        Awaitility.await()
            .during(java.time.Duration.ofMillis(200))
            .with()
            .pollInterval(java.time.Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(emptyList<PresenceEvent>(), presenceEventSink.events)
            }
    }

    @Test
    fun `should not deliver TimesUp event when cancelled before runEffect`() {
        // given
        val waitEffect = WaitEffect(heartbeatInterval, presenceEventSink)

        // when
        waitEffect.cancel()
        waitEffect.runEffect()

        // then
        Awaitility.await()
            .during(java.time.Duration.ofMillis(200))
            .with()
            .pollInterval(java.time.Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(emptyList<PresenceEvent>(), presenceEventSink.events)
            }
    }
}
