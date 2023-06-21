package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.EffectDispatcher
import com.pubnub.api.eventengine.EventSink
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.subscribe.eventengine.event.Event
import org.slf4j.LoggerFactory

class ReceiveMessagesEffect(
    private val remoteAction: RemoteAction<ReceiveMessagesResult>,
    private val eventSink: EventSink,
) : ManagedEffect {
    private val log = LoggerFactory.getLogger(EffectDispatcher::class.java)

    override fun runEffect() {
        log.trace("Running ReceiveMessagesEffect thread: ${Thread.currentThread().id}")

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
