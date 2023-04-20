package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.EffectHandlerFactory
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

interface HandshakeProvider {
    fun handshake(channels: List<String>, channelGroups: List<String>): RemoteAction<SubscriptionCursor>
}

interface EventHandler {
    fun handleEvent(event: Event)
}

class SubscribeEffectHandlerFactory(
    private val handshakeProvider: HandshakeProvider,
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
                                    status.exception ?: PubNubException("Unknown error")
                                )
                            ) // todo check it that can happen
                        } else {
                            eventHandler.handleEvent(Event.HandshakeSuccess(result!!))
                        }
                    }
            }

            is EffectInvocation.HandshakeReconnect -> TODO()
            is EffectInvocation.ReceiveMessages -> TODO()
            is EffectInvocation.ReceiveReconnect -> TODO()
            EffectInvocation.CancelHandshake,
            EffectInvocation.CancelHandshakeReconnect,
            EffectInvocation.CancelReceiveMessages,
            EffectInvocation.CancelReceiveReconnect,
            -> TODO()
        }
    }
}

class RemoteActionManagedEffect<T>(
    private val remoteAction: RemoteAction<T>,
    private val callback: (result: T?, status: PNStatus) -> Unit
) : ManagedEffect {
    override fun run(completionBlock: () -> Unit) {
        remoteAction.async { result, status ->
            try {
                callback(result, status)
            } finally {
                completionBlock()
            }
        }
    }

    override fun cancel() {
        remoteAction.silentCancel()
    }
}

fun <T> RemoteAction<T>.toManagedEffect(callback: (result: T?, status: PNStatus) -> Unit): ManagedEffect {
    return RemoteActionManagedEffect(this, callback)
}
