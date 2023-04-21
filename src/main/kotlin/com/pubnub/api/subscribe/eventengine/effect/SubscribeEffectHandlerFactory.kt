package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.EffectHandlerFactory
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

interface EffectProvider : HandshakeProvider, ReceiveMessagesProvider

fun interface HandshakeProvider {
    fun handshake(channels: List<String>, channelGroups: List<String>): RemoteAction<SubscriptionCursor>
}

fun interface ReceiveMessagesProvider {
    fun receiveMessages(channels: List<String>, channelGroups: List<String>, subscriptionCursor: SubscriptionCursor): RemoteAction<ReceiveMessagesResult>
}

data class ReceiveMessagesResult(
    val messages: List<PNEvent>,
    val subscriptionCursor: SubscriptionCursor
)

interface EventHandler {
    fun handleEvent(event: Event)
}

class SubscribeEffectHandlerFactory(
    private val handshakeProvider: HandshakeProvider,
    private val receiveMessagesProvider: ReceiveMessagesProvider,
    private val eventHandler: EventHandler
) : EffectHandlerFactory<EffectInvocation> {
    override fun create(effectInvocation: EffectInvocation): ManagedEffect {
        return when (effectInvocation) {
            is EffectInvocation.EmitMessages -> TODO()
            is EffectInvocation.EmitStatus -> TODO()
            is EffectInvocation.Handshake -> {
                handshakeProvider.handshake(effectInvocation.channels, effectInvocation.channelGroups)
                    .toManagedEffect { result, status ->
                        if (status.error) {
                            eventHandler.handleEvent(
                                Event.HandshakeFailure(
                                    status.exception ?: PubNubException("Unknown error") // todo check it that can happen
                                )
                            )
                        } else {
                            eventHandler.handleEvent(Event.HandshakeSuccess(result!!))
                        }
                    }
            }

            is EffectInvocation.HandshakeReconnect -> TODO()
            is EffectInvocation.ReceiveMessages -> {
                receiveMessagesProvider.receiveMessages(effectInvocation.channels, effectInvocation.channelGroups, effectInvocation.subscriptionCursor)
                    .toManagedEffect { result, status ->
                        if (status.error) {
                            eventHandler.handleEvent(
                                Event.ReceiveFailure(
                                    status.exception ?: PubNubException("Unknown error") // todo check it that can happen
                                )
                            )
                        } else {
                            eventHandler.handleEvent(Event.ReceiveSuccess(result!!.messages, result.subscriptionCursor))
                        }
                    }
            }
            is EffectInvocation.ReceiveReconnect -> TODO()
            EffectInvocation.CancelHandshake,
            EffectInvocation.CancelHandshakeReconnect,
            EffectInvocation.CancelReceiveMessages,
            EffectInvocation.CancelReceiveReconnect,
            -> TODO()
        }
    }
}
