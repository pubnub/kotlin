package com.pubnub.api.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.presence.eventengine.event.PresenceEvent
import com.pubnub.api.subscribe.eventengine.effect.LinearPolicy
import com.pubnub.api.subscribe.eventengine.effect.NoRetriesPolicy
import com.pubnub.api.subscribe.eventengine.effect.TestEventSink
import com.pubnub.api.subscribe.eventengine.effect.failingRemoteAction
import com.pubnub.api.subscribe.eventengine.effect.successfulRemoteAction
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.Executors

class DelayedHeartbeatEffectTest {
    private val channels = setOf("channel1")
    private val channelGroups = setOf("channelGroup1")
    private val reason = PubNubException("Unknown error")
    private val attempts = 1
    private val presenceEventSink = TestEventSink<PresenceEvent>()
    private val resultFromHeartbeat = true
    private val policy = LinearPolicy(fixedDelay = Duration.ofMillis(10))
    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private val delayedHeartbeatInvocation = PresenceEffectInvocation.DelayedHeartbeat(channels, channelGroups, attempts, reason)

    @Test
    fun `should deliver HeartbeatSuccess event when DelayedHeartbeatEffect succeeded`() {
        // given
        val delayedHeartbeatEffect = DelayedHeartbeatEffect(
            successfulRemoteAction(resultFromHeartbeat),
            presenceEventSink,
            policy,
            executorService,
            delayedHeartbeatInvocation
        )

        // when
        delayedHeartbeatEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(PresenceEvent.HeartbeatSuccess), presenceEventSink.events)
            }
    }

    @Test
    fun `should deliver HeartbeatFailure event when DelayedHeartbeatEffect failed`() {
        // given
        val delayedHeartbeatEffect = DelayedHeartbeatEffect(
            failingRemoteAction(reason),
            presenceEventSink,
            policy,
            executorService,
            delayedHeartbeatInvocation
        )

        // when
        delayedHeartbeatEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(PresenceEvent.HeartbeatFailure(reason)), presenceEventSink.events)
            }
    }

    @Test
    fun `should deliver HeartbeatGiveup event when delay is null`() {
        // given
        val policy = NoRetriesPolicy
        val delayedHeartbeatEffect = DelayedHeartbeatEffect(
            failingRemoteAction(reason),
            presenceEventSink,
            policy,
            executorService,
            delayedHeartbeatInvocation
        )

        // when
        delayedHeartbeatEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(PresenceEvent.HeartbeatGiveup(reason)), presenceEventSink.events)
            }
    }

    @Test
    fun `should cancel remoteAction when cancel DelayedHeartbeatEffect`() {
        // given
        val heartbeatRemoteAction: RemoteAction<Boolean> = mockk()
        val delayedHeartbeatEffect = DelayedHeartbeatEffect(
            heartbeatRemoteAction,
            presenceEventSink,
            policy,
            executorService,
            delayedHeartbeatInvocation
        )
        every { heartbeatRemoteAction.silentCancel() } returns Unit

        // when
        delayedHeartbeatEffect.cancel()

        // then
        verify { heartbeatRemoteAction.silentCancel() }
    }
}
