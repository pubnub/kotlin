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

class HandshakeEffectTest {
    private val channels = listOf("channel1")
    private val channelGroups = listOf("channelGroup1")
    private val handshakeProvider = mockk<HandshakeProvider>()
    private val eventDeliver = SubscribeManagedEffectFactoryTest.TestEventDeliver()
    private val handshakeInvocation = SubscribeEffectInvocation.Handshake(channels, channelGroups)
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")
    private val reason = PubNubException("Unknown error")

    @Test
    fun `should deliver HandshakeSuccess event when HandshakeEffect succeeded `() {
        // given
        every { handshakeProvider.handshake(any(), any()) } returns successfulRemoteAction(subscriptionCursor)
        val handshakeEffect = HandshakeEffect(handshakeProvider, eventDeliver, handshakeInvocation)

        // when
        handshakeEffect.runEffect()

        // then
        Thread.sleep(50)
        assertEquals(listOf(Event.HandshakeSuccess(subscriptionCursor)), eventDeliver.events)
    }

    @Test
    fun `should deliver HandshakeFailure event when HandshakeEffect failed `() {
        // given
        every { handshakeProvider.handshake(any(), any()) } returns failingRemoteAction(reason)
        val handshakeEffect = HandshakeEffect(handshakeProvider, eventDeliver, handshakeInvocation)

        // when
        handshakeEffect.runEffect()

        // then
        Thread.sleep(50)
        assertEquals(listOf(Event.HandshakeFailure(reason)), eventDeliver.events)
    }

    @Test
    fun `should cancel remoteAction when cancel effect`() {
        // given
        val remoteAction = mockk<RemoteAction<SubscriptionCursor>>()
        every { handshakeProvider.handshake(any(), any()) } returns remoteAction
        every { remoteAction.silentCancel() } returns Unit
        val handshakeEffect = HandshakeEffect(handshakeProvider, eventDeliver, handshakeInvocation)

        // when
        handshakeEffect.cancel()

        // then
        verify { remoteAction.silentCancel() }
    }

    // todo add test to check completionBlock execution

    fun runEffect1(completionBlock: () -> Unit) {
        completionBlock.invoke()
    }

    @Test
    fun `should execute completionBock when such block is provided`() {
        // given
        every { handshakeProvider.handshake(any(), any()) } returns successfulRemoteAction(subscriptionCursor)
        val handshakeEffect = HandshakeEffect(handshakeProvider, eventDeliver, handshakeInvocation)
        val completionBlockMock: () -> Unit = mockk()
        every { completionBlockMock.invoke() } returns Unit

        // when
        handshakeEffect.runEffect(completionBlockMock)

        // then
        Thread.sleep(50)
        verify(exactly = 1) { completionBlockMock.invoke() }
    }
}
