package com.pubnub.internal.presence.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.presence.eventengine.event.PresenceEvent
import com.pubnub.internal.subscribe.eventengine.effect.TestEventSink
import com.pubnub.internal.subscribe.eventengine.effect.failingRemoteAction
import com.pubnub.internal.subscribe.eventengine.effect.successfulRemoteAction
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.milliseconds

class DelayedHeartbeatEffectTest {
    private val channels = setOf("channel1")
    private val channelGroups = setOf("channelGroup1")
    private val reason = PubNubException("Unknown error")
    private val attempts = 1
    private val presenceEventSink = TestEventSink<PresenceEvent>()
    private val resultFromHeartbeat = true
    private val retryConfiguration = RetryConfiguration.Linear(delayInSec = 10.milliseconds, isInternal = true)
    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private val delayedHeartbeatInvocation =
        PresenceEffectInvocation.DelayedHeartbeat(channels, channelGroups, attempts, reason)

    @Test
    fun `should deliver HeartbeatSuccess event when DelayedHeartbeatEffect succeeded`() {
        // given
        val delayedHeartbeatEffect =
            DelayedHeartbeatEffect(
                successfulRemoteAction(resultFromHeartbeat),
                presenceEventSink,
                retryConfiguration,
                executorService,
                delayedHeartbeatInvocation.attempts,
                delayedHeartbeatInvocation.reason,
            )

        // when
        delayedHeartbeatEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.FIVE_SECONDS)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(PresenceEvent.HeartbeatSuccess), presenceEventSink.events)
            }
    }

    @Test
    fun `should deliver HeartbeatFailure event when DelayedHeartbeatEffect failed`() {
        // given
        val delayedHeartbeatEffect =
            DelayedHeartbeatEffect(
                failingRemoteAction(reason),
                presenceEventSink,
                retryConfiguration,
                executorService,
                delayedHeartbeatInvocation.attempts,
                delayedHeartbeatInvocation.reason,
            )

        // when
        delayedHeartbeatEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.FIVE_SECONDS)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(PresenceEvent.HeartbeatFailure(reason)), presenceEventSink.events)
            }
    }

    @Test
    fun `should deliver HeartbeatGiveup event when delay is null`() {
        // given
        val policy = RetryConfiguration.None
        val delayedHeartbeatEffect =
            DelayedHeartbeatEffect(
                failingRemoteAction(reason),
                presenceEventSink,
                policy,
                executorService,
                delayedHeartbeatInvocation.attempts,
                delayedHeartbeatInvocation.reason,
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
        val delayedHeartbeatEffect =
            DelayedHeartbeatEffect(
                heartbeatRemoteAction,
                presenceEventSink,
                retryConfiguration,
                executorService,
                delayedHeartbeatInvocation.attempts,
                delayedHeartbeatInvocation.reason,
            )
        every { heartbeatRemoteAction.silentCancel() } returns Unit

        // when
        delayedHeartbeatEffect.cancel()

        // then
        verify { heartbeatRemoteAction.silentCancel() }
    }
}
