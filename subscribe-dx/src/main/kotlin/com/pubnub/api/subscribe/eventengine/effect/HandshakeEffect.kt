package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.core.PubNubException
import com.pubnub.core.CoreRemoteAction
import org.slf4j.LoggerFactory

class HandshakeEffect(
    private val remoteAction: CoreRemoteAction<SubscriptionCursor, *>,
    private val eventSink: Sink<Event>,
) : ManagedEffect {
    private val log = LoggerFactory.getLogger(HandshakeEffect::class.java)

    override fun runEffect() {
        log.trace("Running HandshakeEffect")

        remoteAction.async { result, status ->
            if (status.error) {
                eventSink.add(
                    Event.HandshakeFailure(
                        status.exception
                            ?: PubNubException("Unknown error") // todo check if that can happen
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
