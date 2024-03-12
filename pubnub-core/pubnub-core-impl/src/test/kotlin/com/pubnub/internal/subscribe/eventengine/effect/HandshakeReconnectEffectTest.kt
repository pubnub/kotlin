package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor
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

class HandshakeReconnectEffectTest {
    private val channels = setOf("channel1")
    private val channelGroups = setOf("channelGroup1")
    private val reason = PubNubException("Unknown error")
    private val attempts = 1
    private val subscribeEventSink = TestEventSink<SubscribeEvent>()
    private val handshakeReconnectInvocation =
        SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, attempts, reason)
    private val executorService = Executors.newSingleThreadScheduledExecutor()

    @Suppress("INVISIBLE_MEMBER")
    private val retryConfiguration = RetryConfiguration.Linear(delayInSec = 10.milliseconds, isInternal = true)
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")

    @Test
    fun `should deliver HandshakeReconnectSuccess event when HandshakeReconnectEffect succeeded`() {
        // given
        val handshakeReconnectEffect =
            HandshakeReconnectEffect(
                successfulRemoteAction(subscriptionCursor),
                subscribeEventSink,
                retryConfiguration,
                executorService,
                handshakeReconnectInvocation.attempts,
                handshakeReconnectInvocation.reason,
            )

        // when
        handshakeReconnectEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.FIVE_SECONDS)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(
                    listOf(SubscribeEvent.HandshakeReconnectSuccess(subscriptionCursor)),
                    subscribeEventSink.events,
                )
            }
    }

    @Test
    fun `should deliver HandshakeReconnectFailure event when HandshakeReconnectEffect failed`() {
        // given
        val handshakeReconnectEffect =
            HandshakeReconnectEffect(
                failingRemoteAction(reason),
                subscribeEventSink,
                retryConfiguration,
                executorService,
                handshakeReconnectInvocation.attempts,
                handshakeReconnectInvocation.reason,
            )

        // when
        handshakeReconnectEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.FIVE_SECONDS)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(
                    listOf(SubscribeEvent.HandshakeReconnectFailure(reason)),
                    subscribeEventSink.events,
                )
            }
    }

    @Test
    fun `should deliver HandshakeReconnectGiveUp event when delay is null`() {
        // given
        val policy = RetryConfiguration.None
        val handshakeReconnectEffect =
            HandshakeReconnectEffect(
                failingRemoteAction(reason),
                subscribeEventSink,
                policy,
                executorService,
                handshakeReconnectInvocation.attempts,
                handshakeReconnectInvocation.reason,
            )

        // when
        handshakeReconnectEffect.runEffect()

        // then
        assertEquals(listOf(SubscribeEvent.HandshakeReconnectGiveup(reason)), subscribeEventSink.events)
    }

    @Test
    fun `should cancel remoteAction when cancel effect`() {
        // given
        val remoteAction = mockk<RemoteAction<SubscriptionCursor>>()
        every { remoteAction.silentCancel() } returns Unit
        val handshakeReconnectEffect =
            HandshakeReconnectEffect(
                remoteAction,
                subscribeEventSink,
                retryConfiguration,
                executorService,
                handshakeReconnectInvocation.attempts,
                handshakeReconnectInvocation.reason,
            )

        // when
        handshakeReconnectEffect.cancel()

        // then
        verify { remoteAction.silentCancel() }
    }
}
