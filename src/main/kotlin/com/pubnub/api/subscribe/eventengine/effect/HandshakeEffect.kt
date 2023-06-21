package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.EventSink
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import org.slf4j.LoggerFactory

class HandshakeEffect(
    private val remoteAction: RemoteAction<SubscriptionCursor>,
    private val eventSink: EventSink,
) : ManagedEffect {
    private val log = LoggerFactory.getLogger(EffectDispatcher::class.java)

    override fun runEffect() {
        log.trace("Running HandshakeEffect thread: ${Thread.currentThread().id}")

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
