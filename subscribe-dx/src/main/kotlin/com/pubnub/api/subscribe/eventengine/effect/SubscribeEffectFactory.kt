package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.eventengine.Effect
import com.pubnub.api.eventengine.EffectFactory
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.HandshakeProvider
import com.pubnub.api.subscribe.eventengine.effect.effectprovider.ReceiveMessagesProvider
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.core.Status
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

data class ReceiveMessagesResult(
    val messages: List<PNEvent>, val subscriptionCursor: SubscriptionCursor
)

internal class SubscribeEffectFactory(
    private val handshakeProvider: HandshakeProvider,
    private val receiveMessagesProvider: ReceiveMessagesProvider,
    private val eventSink: Sink<Event>,
    private val policy: RetryPolicy,
    private val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(),
    private val messagesConsumer: MessagesConsumer,
    private val statusConsumer: StatusConsumer,
) : EffectFactory<SubscribeEffectInvocation> {
    override fun create(effectInvocation: SubscribeEffectInvocation): Effect? {
        return when (effectInvocation) {
            is SubscribeEffectInvocation.EmitMessages -> {
                EmitMessagesEffect(messagesConsumer, effectInvocation.messages)
            }

            is SubscribeEffectInvocation.EmitStatus -> {
                EmitStatusEffect(statusConsumer, effectInvocation.status)
            }

            is SubscribeEffectInvocation.Handshake -> {
                val handshakeRemoteAction = handshakeProvider.getHandshakeRemoteAction(
                    effectInvocation.channels, effectInvocation.channelGroups
                )
                HandshakeEffect(handshakeRemoteAction, eventSink)
            }

            is SubscribeEffectInvocation.HandshakeReconnect -> {
                val handshakeRemoteAction = handshakeProvider.getHandshakeRemoteAction(
                    effectInvocation.channels, effectInvocation.channelGroups
                )
                HandshakeReconnectEffect(
                    handshakeRemoteAction, eventSink, policy, executorService, effectInvocation
                )
            }

            is SubscribeEffectInvocation.ReceiveMessages -> {
                val receiveMessagesRemoteAction = receiveMessagesProvider.getReceiveMessagesRemoteAction(
                    effectInvocation.channels, effectInvocation.channelGroups, effectInvocation.subscriptionCursor
                )
                ReceiveMessagesEffect(receiveMessagesRemoteAction, eventSink)
            }

            is SubscribeEffectInvocation.ReceiveReconnect -> {
                val receiveMessagesRemoteAction = receiveMessagesProvider.getReceiveMessagesRemoteAction(
                    effectInvocation.channels, effectInvocation.channelGroups, effectInvocation.subscriptionCursor
                )

                ReceiveReconnectEffect(
                    receiveMessagesRemoteAction,
                    eventSink,
                    policy,
                    executorService,
                    effectInvocation.attempts,
                    effectInvocation.reason
                )
            }

            SubscribeEffectInvocation.CancelHandshake,
            SubscribeEffectInvocation.CancelHandshakeReconnect,
            SubscribeEffectInvocation.CancelReceiveMessages,
            SubscribeEffectInvocation.CancelReceiveReconnect,
            -> null
        }
    }
}
