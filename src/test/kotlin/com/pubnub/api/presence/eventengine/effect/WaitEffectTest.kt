package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.TestEventSink
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.Timer

class WaitEffectTest {
    private val heartbeatIntervalInSec: Int = 1
    private val presenceEventSink = TestEventSink<PresenceEvent>()

    @Test
    fun `should deliver TimesUp event when WaitEffect finishes successfully`() {
        // given
        val waitEffect = WaitEffect(heartbeatIntervalInSec, presenceEventSink)

        // when
        waitEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(PresenceEvent.TimesUp), presenceEventSink.events)
            }
    }

    @Test
    fun `should cancel timer when cancel effect`() {
        // given
        val timer: Timer = mockk()
        val waitEffect = WaitEffect(heartbeatIntervalInSec, presenceEventSink, timer)
        every { timer.cancel() } returns Unit

        // when
        waitEffect.cancel()

        // then
        verify { timer.cancel() }
    }
}
