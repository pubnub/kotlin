package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.Effect
import com.pubnub.api.eventengine.EffectFactory
import com.pubnub.api.eventengine.EventSink
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
    private val eventSink: EventSink,
    private val policy: RetryPolicy,
    private val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(),
    private val messagesConsumer: MessagesConsumer,
) : EffectFactory<SubscribeEffectInvocation> {
    override fun create(effectInvocation: SubscribeEffectInvocation): Effect? {
        return when (effectInvocation) {
            is SubscribeEffectInvocation.EmitMessages -> {
                EmitMessagesEffect(messagesConsumer, effectInvocation.messages)
            }
            is SubscribeEffectInvocation.EmitStatus -> null // todo
            is SubscribeEffectInvocation.Handshake -> {
                val handshakeRemoteAction =
                    handshakeProvider.getHandshakeRemoteAction(
                        effectInvocation.channels,
                        effectInvocation.channelGroups
                    )
                HandshakeEffect(handshakeRemoteAction, eventSink)
            }
            is SubscribeEffectInvocation.HandshakeReconnect -> {
                val handshakeRemoteAction =
                    handshakeProvider.getHandshakeRemoteAction(
                        effectInvocation.channels,
                        effectInvocation.channelGroups
                    )
                HandshakeReconnectEffect(
                    handshakeRemoteAction,
                    eventSink,
                    policy,
                    executorService,
                    effectInvocation
                )
            }
            is SubscribeEffectInvocation.ReceiveMessages -> {
                val receiveMessagesRemoteAction: RemoteAction<ReceiveMessagesResult> =
                    receiveMessagesProvider.getReceiveMessagesRemoteAction(
                        effectInvocation.channels,
                        effectInvocation.channelGroups,
                        effectInvocation.subscriptionCursor
                    )
                ReceiveMessagesEffect(receiveMessagesRemoteAction, eventSink)
            }
            is SubscribeEffectInvocation.ReceiveReconnect -> {
                val receiveMessagesRemoteAction = receiveMessagesProvider.getReceiveMessagesRemoteAction(
                    effectInvocation.channels,
                    effectInvocation.channelGroups,
                    effectInvocation.subscriptionCursor
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
