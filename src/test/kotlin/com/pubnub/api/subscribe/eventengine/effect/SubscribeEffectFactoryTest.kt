package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.HandshakeProvider
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.ReceiveMessagesProvider
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.ScheduledExecutorService

class SubscribeEffectFactoryTest {

    private val handshakeProvider = mockk<HandshakeProvider>()
    private val receiveMessageProvider = mockk<ReceiveMessagesProvider>()
    private val messagesConsumer = mockk<MessagesConsumer>()
    private val eventSink = mockk<Sink<Event>>()
    private val policy = mockk<RetryPolicy>()
    private val executorService = mockk<ScheduledExecutorService>()
    private val channels = listOf("channel1")
    private val channelGroups = listOf("channelGroup1")
    private lateinit var subscribeEffectFactory: SubscribeEffectFactory
    private val attempts = 1
    private val reason = PubNubException("Unknown error")
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")
    private val handshakeRemoteAction: RemoteAction<SubscriptionCursor> = mockk()
    private val receiveMessagesRemoteAction: RemoteAction<ReceiveMessagesResult> = mockk()
    private val statusConsumer = mockk<StatusConsumer>()

    @BeforeEach
    fun setUp() {
        subscribeEffectFactory = SubscribeEffectFactory(
            handshakeProvider,
            receiveMessageProvider,
            eventSink,
            policy,
            executorService,
            messagesConsumer,
            statusConsumer
        )
    }

    @Test
    fun `should return emitMessages effect when getting EmitMessages invocation`() {
        // when
        val effect = subscribeEffectFactory.create(SubscribeEffectInvocation.EmitMessages(messages = listOf()))

        // then
        assertThat(effect, instanceOf(EmitMessagesEffect::class.java))
    }

    @Test
    fun `should return emitStatus effect when getting EmitStatus invocation`() {
        // when
        val effect = subscribeEffectFactory.create(
            SubscribeEffectInvocation.EmitStatus(
                status =
                PNStatus(
                    category = PNStatusCategory.PNConnectedCategory,
                    operation = PNOperationType.PNSubscribeOperation,
                    error = false,
                    affectedChannels = channels,
                    affectedChannelGroups = channelGroups
                )
            )
        )

        // then
        assertThat(effect, instanceOf(EmitStatusEffect::class.java))
    }

    @Test
    fun `should return handshake effect when getting Handshake invocation`() {
        // given
        val effectInvocation = SubscribeEffectInvocation.Handshake(channels, channelGroups)
        every {
            handshakeProvider.getHandshakeRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups
            )
        } returns handshakeRemoteAction
        // when
        val managedEffect =
            subscribeEffectFactory.create(SubscribeEffectInvocation.Handshake(channels, channelGroups))

        // then
        assertThat(managedEffect, instanceOf(HandshakeEffect::class.java))
        assertThat(managedEffect, instanceOf(ManagedEffect::class.java))
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
            handshakeProvider.getHandshakeRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups
            )
        } returns handshakeRemoteAction

        // when
        val managedEffect = subscribeEffectFactory.create(
            SubscribeEffectInvocation.HandshakeReconnect(
                channels,
                channelGroups,
                attempts,
                reason
            )
        )

        // then
        assertThat(managedEffect, instanceOf(HandshakeReconnectEffect::class.java))
        assertThat(managedEffect, instanceOf(ManagedEffect::class.java))
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
            receiveMessageProvider.getReceiveMessagesRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                effectInvocation.subscriptionCursor
            )
        } returns receiveMessagesRemoteAction

        // when
        val managedEffect = subscribeEffectFactory.create(
            SubscribeEffectInvocation.ReceiveMessages(
                channels,
                channelGroups,
                subscriptionCursor
            )
        )

        // then
        assertThat(managedEffect, instanceOf(ReceiveMessagesEffect::class.java))
        assertThat(managedEffect, instanceOf(ManagedEffect::class.java))
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
            receiveMessageProvider.getReceiveMessagesRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                effectInvocation.subscriptionCursor
            )
        } returns receiveMessagesRemoteAction

        // when
        val managedEffect = subscribeEffectFactory.create(
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
        assertThat(managedEffect, instanceOf(ManagedEffect::class.java))
    }

    @Test
    fun `should return null when getting CancelHandshake invocation`() {
        // when
        val managedEffect = subscribeEffectFactory.create(SubscribeEffectInvocation.CancelHandshake)

        // then
        assertNull(managedEffect)
    }

    @Test
    fun `should return null when getting CancelHandshakeReconnect invocation`() {
        // when
        val managedEffect = subscribeEffectFactory.create(SubscribeEffectInvocation.CancelHandshakeReconnect)

        // then
        assertNull(managedEffect)
    }

    @Test
    fun `should return null when getting CancelReceiveMessages invocation`() {
        // when
        val managedEffect = subscribeEffectFactory.create(SubscribeEffectInvocation.CancelReceiveMessages)

        // then
        assertNull(managedEffect)
    }

    @Test
    fun `should return null when getting CancelReceiveReconnect invocation`() {
        // when
        val managedEffect = subscribeEffectFactory.create(SubscribeEffectInvocation.CancelReceiveReconnect)

        // then
        assertNull(managedEffect)
    }
}
