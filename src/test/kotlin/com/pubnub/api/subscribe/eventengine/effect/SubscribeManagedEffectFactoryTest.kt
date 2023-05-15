package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.EventQueue
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.ScheduledExecutorService

class SubscribeManagedEffectFactoryTest {

    private val handshakeProvider = mockk<HandshakeProvider>()
    private val receiveMessageProvider = mockk<ReceiveMessagesProvider>()
    private val eventQueue = mockk<EventQueue>()
    private val policy = mockk<RetryPolicy>()
    private val executorService = mockk<ScheduledExecutorService>()
    private val channels = listOf("channel1")
    private val channelGroups = listOf("channelGroup1")
    private lateinit var subscribeManagedEffectFactory: SubscribeManagedEffectFactory
    private val attempts = 1
    private val reason = PubNubException("Unknown error")
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")
    private val remoteActionForHandshake: RemoteAction<SubscriptionCursor> = mockk()
    private val remoteActionForReceiveMessages: RemoteAction<ReceiveMessagesResult> = mockk()

    @BeforeEach
    fun setUp() {
        subscribeManagedEffectFactory = SubscribeManagedEffectFactory(
            handshakeProvider,
            receiveMessageProvider,
            eventQueue,
            policy,
            executorService
        )
    }

    @Test
    fun `should return handshake effect when getting Handshake invocation`() {
        // given
        val effectInvocation = SubscribeEffectInvocation.Handshake(channels, channelGroups)
        every {
            handshakeProvider.getRemoteActionForHandshake(
                effectInvocation.channels,
                effectInvocation.channelGroups
            )
        } returns remoteActionForHandshake
        // when
        val managedEffect =
            subscribeManagedEffectFactory.create(effectInvocation)

        // then
        assertThat(managedEffect, instanceOf(HandshakeEffect::class.java))
    }

    @Test
    fun `should return handshakeReconnect effect when getting HandshakeReconnect invocation`() {
        // given
        val effectInvocation = SubscribeEffectInvocation.HandshakeReconnect(
            channels,
            channelGroups,
            attempts,
            reason
        )
        every {
            handshakeProvider.getRemoteActionForHandshake(
                effectInvocation.channels,
                effectInvocation.channelGroups
            )
        } returns remoteActionForHandshake

        // when

        val managedEffect = subscribeManagedEffectFactory.create(effectInvocation)

        // then
        assertThat(managedEffect, instanceOf(HandshakeReconnectEffect::class.java))
    }

    @Test
    fun `should return receiveMessages effect when getting ReceiveMessages invocation`() {
        // given
        val effectInvocation = SubscribeEffectInvocation.ReceiveMessages(
            channels,
            channelGroups,
            subscriptionCursor
        )
        every {
            receiveMessageProvider.getRemoteActionForReceiveMessages(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                effectInvocation.subscriptionCursor
            )
        } returns remoteActionForReceiveMessages

        // when

        val managedEffect = subscribeManagedEffectFactory.create(effectInvocation)

        // then
        assertThat(managedEffect, instanceOf(ReceiveMessagesEffect::class.java))
    }

    @Test
    fun `should return receiveReconnect effect when getting ReceiveReconnect invocation`() {
        // given
        val effectInvocation = SubscribeEffectInvocation.ReceiveMessages(
            channels,
            channelGroups,
            subscriptionCursor
        )
        every {
            receiveMessageProvider.getRemoteActionForReceiveMessages(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                effectInvocation.subscriptionCursor
            )
        } returns remoteActionForReceiveMessages

        // when
        val managedEffect = subscribeManagedEffectFactory.create(
            SubscribeEffectInvocation.ReceiveReconnect(
                channels,
                channelGroups,
                subscriptionCursor,
                attempts,
                reason
            )
        )

        // then
        assertThat(managedEffect, instanceOf(ReceiveReconnectEffect::class.java))
    }

    @Test
    fun `should return null when getting CancelHandshake invocation`() {
        // when
        val managedEffect = subscribeManagedEffectFactory.create(SubscribeEffectInvocation.CancelHandshake)

        // then
        assertNull(managedEffect)
    }

    @Test
    fun `should return null when getting CancelHandshakeReconnect invocation`() {
        // when
        val managedEffect = subscribeManagedEffectFactory.create(SubscribeEffectInvocation.CancelHandshakeReconnect)

        // then
        assertNull(managedEffect)
    }

    @Test
    fun `should return null when getting CancelReceiveMessages invocation`() {
        // when
        val managedEffect = subscribeManagedEffectFactory.create(SubscribeEffectInvocation.CancelReceiveMessages)

        // then
        assertNull(managedEffect)
    }

    @Test
    fun `should return null when getting CancelReceiveReconnect invocation`() {
        // when
        val managedEffect = subscribeManagedEffectFactory.create(SubscribeEffectInvocation.CancelReceiveReconnect)

        // then
        assertNull(managedEffect)
    }
}
