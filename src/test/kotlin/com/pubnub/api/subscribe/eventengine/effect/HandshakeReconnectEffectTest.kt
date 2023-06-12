package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.Executors

class HandshakeReconnectEffectTest {
    private val channels = listOf("channel1")
    private val channelGroups = listOf("channelGroup1")
    private val reason = PubNubException("Unknown error")
    private val attempts = 1
    private val eventSink = TestEventSink()
    private val handshakeReconnectInvocation =
        SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, attempts, reason)
    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private val policy = LinearPolicy(fixedDelay = Duration.ofMillis(10))
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")

    @Test
    fun `should deliver HandshakeReconnectSuccess event when HandshakeReconnectEffect succeeded`() {
        // given
        val handshakeReconnectEffect = HandshakeReconnectEffect(
            successfulRemoteAction(subscriptionCursor),
            eventSink,
            policy,
            executorService,
            handshakeReconnectInvocation
        )

        // when
        handshakeReconnectEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted {
                assertEquals(listOf(Event.HandshakeReconnectSuccess(subscriptionCursor)), eventSink.events)
            }
    }

    @Test
    fun `should deliver HandshakeReconnectFailure event when HandshakeReconnectEffect failed`() {
        // given
        val handshakeReconnectEffect = HandshakeReconnectEffect(
            failingRemoteAction(reason),
            eventSink,
            policy,
            executorService,
            handshakeReconnectInvocation
        )

        // when
        handshakeReconnectEffect.runEffect()

        // then
        Awaitility.await()
            .atMost(Durations.ONE_SECOND)
            .with()
            .pollInterval(Duration.ofMillis(20))
            .untilAsserted { assertEquals(listOf(Event.HandshakeReconnectFailure(reason)), eventSink.events) }
    }

    @Test
    fun `should deliver HandshakeReconnectGiveUp event when delay is null`() {
        // given
        val policy = NoRetriesPolicy
        val handshakeReconnectEffect = HandshakeReconnectEffect(
            failingRemoteAction(reason),
            eventSink,
            policy,
            executorService,
            handshakeReconnectInvocation
        )

        // when
        handshakeReconnectEffect.runEffect()

        // then
        assertEquals(listOf(Event.HandshakeReconnectGiveUp(reason)), eventSink.events)
    }

    @Test
    fun `should cancel remoteAction when cancel effect`() {
        // given
        val remoteAction = mockk<RemoteAction<SubscriptionCursor>>()
        every { remoteAction.silentCancel() } returns Unit
        val handshakeReconnectEffect = HandshakeReconnectEffect(
            remoteAction,
            eventSink,
            policy,
            executorService,
            handshakeReconnectInvocation
        )

        // when
        handshakeReconnectEffect.cancel()

        // then
        verify { remoteAction.silentCancel() }
    }
}
