package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.eventengine.EventDeliver
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.ManagedEffectFactory
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

data class ReceiveMessagesResult(
    val messages: List<PNEvent>,
    val subscriptionCursor: SubscriptionCursor
)

class SubscribeManagedEffectFactory(
    private val handshakeProvider: HandshakeProvider,
    private val receiveMessagesProvider: ReceiveMessagesProvider,
    private val eventDeliver: EventDeliver,
    private val policy: RetryPolicy,
    private val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(),
) : ManagedEffectFactory<SubscribeEffectInvocation> {
    override fun create(effectInvocation: SubscribeEffectInvocation): ManagedEffect? {
        return when (effectInvocation) {
            is SubscribeEffectInvocation.EmitMessages -> null // todo
            is SubscribeEffectInvocation.EmitStatus -> null // todo
            is SubscribeEffectInvocation.Handshake -> {
                HandshakeEffect(handshakeProvider, eventDeliver, effectInvocation)
            }
            is SubscribeEffectInvocation.HandshakeReconnect -> {
                HandshakeReconnectEffect(handshakeProvider, eventDeliver, policy, effectInvocation, executorService)
            }
            is SubscribeEffectInvocation.ReceiveMessages -> {
                ReceiveMessagesEffect(receiveMessagesProvider, eventDeliver, effectInvocation)
            }
            is SubscribeEffectInvocation.ReceiveReconnect -> {
                ReceiveReconnectEffect(receiveMessagesProvider, eventDeliver, policy, effectInvocation, executorService)
            }

            SubscribeEffectInvocation.CancelHandshake,
            SubscribeEffectInvocation.CancelHandshakeReconnect,
            SubscribeEffectInvocation.CancelReceiveMessages,
            SubscribeEffectInvocation.CancelReceiveReconnect,
            -> null
        }
    }
}
