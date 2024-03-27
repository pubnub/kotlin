package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.subscribe.eventengine.effect.TestEventSink
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import kotlin.time.Duration.Companion.milliseconds

class WaitEffectTest {
    private val heartbeatInterval = 1.milliseconds
    private val presenceEventSink = TestEventSink<PresenceEvent>()

    companion object {
        private val executorService: ScheduledExecutorService = Executors.newScheduledThreadPool(10)

        @JvmStatic
        @AfterAll
        fun tearDown() {
            executorService.shutdownNow()
        }
    }

    @Test
    fun `should deliver TimesUp event when WaitEffect finishes successfully`() {
        // given
        val waitEffect = WaitEffect(heartbeatInterval, presenceEventSink, executorService)

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
        val waitEffect = WaitEffect(20.milliseconds, presenceEventSink, executorService)

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
        val waitEffect = WaitEffect(heartbeatInterval, presenceEventSink, executorService)

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
