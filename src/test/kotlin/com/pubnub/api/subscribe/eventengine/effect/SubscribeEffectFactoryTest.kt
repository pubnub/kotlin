package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.presence.eventengine.data.PresenceData
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.HandshakeProvider
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.ReceiveMessagesProvider
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    private val subscribeEventSink = mockk<Sink<SubscribeEvent>>()
    private val retryConfiguration = RetryConfiguration.Linear()
    private val executorService = mockk<ScheduledExecutorService>()
    private val channels = setOf("channel1")
    private val channelGroups = setOf("channelGroup1")
    private lateinit var subscribeEffectFactory: SubscribeEffectFactory
    private val attempts = 1
    private val reason = PubNubException("Unknown error")
    private val subscriptionCursor = SubscriptionCursor(1337L, "1337")
    private val handshakeRemoteAction: RemoteAction<SubscriptionCursor> = mockk()
    private val receiveMessagesRemoteAction: RemoteAction<ReceiveMessagesResult> = mockk()
    private val statusConsumer = mockk<StatusConsumer>()
    private val presenceData = PresenceData()

    @BeforeEach
    fun setUp() {
        presenceData.channelStates.clear()
        subscribeEffectFactory = SubscribeEffectFactory(
            handshakeProvider,
            receiveMessageProvider,
            subscribeEventSink,
            retryConfiguration,
            executorService,
            messagesConsumer,
            statusConsumer,
            presenceData,
            true,
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
                    affectedChannels = channels.toList(),
                    affectedChannelGroups = channelGroups.toList()
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
                effectInvocation.channelGroups,
                any()
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
    fun `should include state from PresenceData into handshake effect when getting Handshake invocation`() {
        // given
        presenceData.channelStates[channels.first()] = mapOf("aaa" to "bbb")
        presenceData.channelStates["nonSubscribedChannel"] = mapOf("aaa" to "bbb")

        val effectInvocation = SubscribeEffectInvocation.Handshake(channels, channelGroups)
        every {
            handshakeProvider.getHandshakeRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                any()
            )
        } returns handshakeRemoteAction

        // when
        subscribeEffectFactory.create(SubscribeEffectInvocation.Handshake(channels, channelGroups))

        // then
        verify {
            handshakeProvider.getHandshakeRemoteAction(
                effectInvocation.channels, effectInvocation.channelGroups,
                mapOf(
                    "channel1" to mapOf(
                        "aaa" to "bbb"
                    )
                )
            )
        }
    }

    @Test
    fun `should not include state from PresenceData into handshake effect when sendStateWithSubscribe == false`() {
        // given
        subscribeEffectFactory = SubscribeEffectFactory(
            handshakeProvider,
            receiveMessageProvider,
            subscribeEventSink,
            retryConfiguration,
            executorService,
            messagesConsumer,
            statusConsumer,
            presenceData,
            false,
        )
        presenceData.channelStates[channels.first()] = mapOf("aaa" to "bbb")
        val effectInvocation = SubscribeEffectInvocation.Handshake(channels, channelGroups)
        every {
            handshakeProvider.getHandshakeRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                any()
            )
        } returns handshakeRemoteAction

        // when
        subscribeEffectFactory.create(SubscribeEffectInvocation.Handshake(channels, channelGroups))

        // then
        verify {
            handshakeProvider.getHandshakeRemoteAction(
                effectInvocation.channels, effectInvocation.channelGroups, null
            )
        }
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
                effectInvocation.channelGroups,
                any()
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
    fun `should include state from PresenceData into handshake effect when getting Handshake reconnect invocation`() {
        // given
        presenceData.channelStates[channels.first()] = mapOf("aaa" to "bbb")
        presenceData.channelStates["nonSubscribedChannel"] = mapOf("aaa" to "bbb")

        val effectInvocation = SubscribeEffectInvocation.HandshakeReconnect(
            channels,
            channelGroups,
            attempts,
            reason
        )
        every {
            handshakeProvider.getHandshakeRemoteAction(
                effectInvocation.channels,
                effectInvocation.channelGroups,
                any()
            )
        } returns handshakeRemoteAction

        // when
        subscribeEffectFactory.create(
            SubscribeEffectInvocation.HandshakeReconnect(
                channels,
                channelGroups,
                attempts,
                reason
            )
        )

        // then
        verify {
            handshakeProvider.getHandshakeRemoteAction(
                effectInvocation.channels, effectInvocation.channelGroups,
                mapOf(
                    "channel1" to mapOf(
                        "aaa" to "bbb"
                    )
                )
            )
        }
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
