package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.TestEventSink
import com.pubnub.api.subscribe.eventengine.effect.failingRemoteAction
import com.pubnub.api.subscribe.eventengine.effect.successfulRemoteAction
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration

class HeartbeatEffectTest {
    private val eventSink = TestEventSink<PresenceEvent>()
    private val reason = PubNubException("Unknown error")

    @Test
    fun `should deliver HeartbeatSuccess event when HeartbeatEffect succeeded`() {
        // given
        val heartbeatEffect = HeartbeatEffect(successfulRemoteAction(true), eventSink)

        // when
        heartbeatEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(PresenceEvent.HeartbeatSuccess), eventSink.events)
            }
    }

    @Test
    fun `should deliver HeartbeatFailure event when HeartbeatEffect failed`() {
        // given
        val heartbeatEffect = HeartbeatEffect(failingRemoteAction(reason), eventSink)
        // when
        heartbeatEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(PresenceEvent.HeartbeatFailure(reason)), eventSink.events)
            }
    }
}
