package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.EventDeliver
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

class HandshakeEffect(
    private val handshakeProvider: HandshakeProvider,
    private val eventDeliver: EventDeliver,
    private val handshakeInvocation: SubscribeEffectInvocation.Handshake
) : ManagedEffect {
    override fun runEffect(completionBlock: () -> Unit) {
        val handshakeRemoteAction = getHandshakeRemoteAction()
        handshakeRemoteAction.async { result, status ->
            try {
                if (status.error) {
                    eventDeliver.passEventForHandling(
                        Event.HandshakeFailure(
                            status.exception
                                ?: PubNubException("Unknown error") // todo check it that can happen
                        )
                    )
                } else {
                    eventDeliver.passEventForHandling(Event.HandshakeSuccess(result!!))
                }
            } finally {
                completionBlock()
            }
        }
    }

    override fun cancel() {
        val handshakeRemoteAction = getHandshakeRemoteAction()
        handshakeRemoteAction.silentCancel()
    }

    private fun getHandshakeRemoteAction(): RemoteAction<SubscriptionCursor> =
        handshakeProvider.handshake(handshakeInvocation.channels, handshakeInvocation.channelGroups)
}
