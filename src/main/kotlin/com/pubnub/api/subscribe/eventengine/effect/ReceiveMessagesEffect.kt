package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import org.slf4j.LoggerFactory

class ReceiveMessagesEffect(
    private val remoteAction: RemoteAction<ReceiveMessagesResult>,
    private val subscribeEventSink: Sink<SubscribeEvent>,
) : ManagedEffect {
    private val log = LoggerFactory.getLogger(ReceiveMessagesEffect::class.java)

    override fun runEffect() {
        log.trace("Running ReceiveMessagesEffect")

        remoteAction.async { result, status ->
            if (status.error) {
                subscribeEventSink.add(
                    SubscribeEvent.ReceiveFailure(
                        status.exception
                            ?: PubNubException("Unknown error") // todo check it that can happen
                    )
                )
            } else {
                subscribeEventSink.add(SubscribeEvent.ReceiveSuccess(result!!.messages, result.subscriptionCursor))
            }
        }
    }

    override fun cancel() {
        remoteAction.silentCancel()
    }
}
