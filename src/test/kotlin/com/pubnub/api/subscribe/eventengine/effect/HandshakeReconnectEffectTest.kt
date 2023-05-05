package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.Executors

class HandshakeReconnectEffectTest {
    private val channels = listOf("channel1")
    private val channelGroups = listOf("channelGroup1")
    private val reason = PubNubException("Unknown error")
    private val attempts = 1
    private val handshakeProvider = mockk<HandshakeProvider>()
    private val eventDeliver = SubscribeManagedEffectFactoryTest.TestEventDeliver()
    private val handshakeReconnectInvocation =
        SubscribeEffectInvocation.HandshakeReconnect(channels, channelGroups, attempts, reason)
    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private val policy = mockk<RetryPolicy>()
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")

    @Test
    fun `should deliver HandshakeReconnectSuccess event when HandshakeReconnectEffect succeeded`() {
        // given
        every { handshakeProvider.handshake(any(), any()) } returns successfulRemoteAction(subscriptionCursor)
        every { policy.nextDelay(any()) } returns Duration.ofMillis(10)
        val handshakeReconnectEffect = HandshakeReconnectEffect(
            handshakeProvider,
            eventDeliver,
            policy,
            handshakeReconnectInvocation,
            executorService
        )

        // when
        handshakeReconnectEffect.runEffect()

        // then
        Thread.sleep(50)
        assertEquals(
            listOf(Event.HandshakeReconnectSuccess(channels, channelGroups, subscriptionCursor)),
            eventDeliver.events
        )
    }

    @Test
    fun `should deliver HandshakeReconnectFailure event when HandshakeReconnectEffect failed`() {
        // given
        every { handshakeProvider.handshake(any(), any()) } returns failingRemoteAction(reason)
        every { policy.nextDelay(any()) } returns Duration.ofMillis(10)
        val handshakeReconnectEffect = HandshakeReconnectEffect(
            handshakeProvider,
            eventDeliver,
            policy,
            handshakeReconnectInvocation,
            executorService
        )

        // when
        handshakeReconnectEffect.runEffect()

        // then
        Thread.sleep(50)
        assertEquals(listOf(Event.HandshakeReconnectFailure(reason)), eventDeliver.events)
    }

    @Test
    fun `should deliver HandshakeReconnectGiveUp event when delay is null`() {
        // given
        every { policy.nextDelay(any()) } returns null
        val handshakeReconnectEffect = HandshakeReconnectEffect(
            handshakeProvider,
            eventDeliver,
            policy,
            handshakeReconnectInvocation,
            executorService
        )

        // when
        handshakeReconnectEffect.runEffect()

        // then
        Thread.sleep(50)
        assertEquals(listOf(Event.HandshakeReconnectGiveUp(reason)), eventDeliver.events)
    }

    @Test
    fun `should cancel remoteAction when cancel effect`() {
        // given
        val remoteAction = mockk<RemoteAction<SubscriptionCursor>>()
        every { handshakeProvider.handshake(any(), any()) } returns remoteAction
        every { remoteAction.silentCancel() } returns Unit
        val handshakeReconnectEffect = HandshakeReconnectEffect(
            handshakeProvider,
            eventDeliver,
            policy,
            handshakeReconnectInvocation,
            executorService
        )

        // when
        handshakeReconnectEffect.cancel()

        // then
        verify { remoteAction.silentCancel() }
    }

    @Test
    fun `should execute completionBock when such block is provided `() {
        // given
        every { handshakeProvider.handshake(any(), any()) } returns successfulRemoteAction(subscriptionCursor)
        every { policy.nextDelay(any()) } returns Duration.ofMillis(10)
        val handshakeReconnectEffect = HandshakeReconnectEffect(
            handshakeProvider,
            eventDeliver,
            policy,
            handshakeReconnectInvocation,
            executorService
        )
        val completionBlockMock: () -> Unit = mockk()
        every { completionBlockMock.invoke() } returns Unit

        // when
        handshakeReconnectEffect.runEffect(completionBlockMock)

        // then
        Thread.sleep(50)
        verify(exactly = 1) { completionBlockMock.invoke() }
    }
}
