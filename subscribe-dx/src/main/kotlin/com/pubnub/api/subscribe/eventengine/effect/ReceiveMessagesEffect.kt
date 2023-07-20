package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.subscribe.eventengine.event.Event
import com.pubnub.core.PubNubException
import com.pubnub.core.CoreRemoteAction
import org.slf4j.LoggerFactory

class ReceiveMessagesEffect(
    private val remoteAction: CoreRemoteAction<ReceiveMessagesResult, *>,
    private val eventSink: Sink<Event>,
) : ManagedEffect {
    private val log = LoggerFactory.getLogger(ReceiveMessagesEffect::class.java)

    override fun runEffect() {
        log.trace("Running ReceiveMessagesEffect")

        remoteAction.async { result, status ->
            if (status.error) {
                eventSink.add(
                    Event.ReceiveFailure(
                        status.exception
                            ?: PubNubException("Unknown error") // todo check it that can happen
                    )
                )
            } else {
                eventSink.add(Event.ReceiveSuccess(result!!.messages, result.subscriptionCursor))
            }
        }
    }

    override fun cancel() {
        remoteAction.silentCancel()
    }
}
