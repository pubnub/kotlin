package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.EventSink
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

class HandshakeEffect(
    private val remoteAction: RemoteAction<SubscriptionCursor>,
    private val eventSink: EventSink,
) : ManagedEffect {
    override fun runEffect() {
        remoteAction.async { result, status ->
            if (status.error) {
                eventSink.add(
                    Event.HandshakeFailure(
                        status.exception
                            ?: PubNubException("Unknown error") // todo check it that can happen
                    )
                )
            } else {
                eventSink.add(Event.HandshakeSuccess(result!!))
            }
        }
    }

    override fun cancel() {
        remoteAction.silentCancel()
    }
}
