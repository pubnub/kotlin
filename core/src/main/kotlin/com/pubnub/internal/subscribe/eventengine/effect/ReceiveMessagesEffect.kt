package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.internal.eventengine.ManagedEffect
import com.pubnub.internal.eventengine.Sink
import com.pubnub.internal.subscribe.eventengine.event.SubscribeEvent
import org.slf4j.LoggerFactory

internal class ReceiveMessagesEffect(
    private val receiveMessagesRemoteAction: RemoteAction<ReceiveMessagesResult>,
    private val subscribeEventSink: Sink<SubscribeEvent>,
) : ManagedEffect {
    private val log = LoggerFactory.getLogger(ReceiveMessagesEffect::class.java)

    override fun runEffect() {
        log.trace("Running ReceiveMessagesEffect")

        receiveMessagesRemoteAction.async { result, status ->
            if (status.error) {
                subscribeEventSink.add(
                    SubscribeEvent.ReceiveFailure(
                        status.exception
                            ?: PubNubException("Unknown error")
                    )
                )
            } else {
                subscribeEventSink.add(SubscribeEvent.ReceiveSuccess(result!!.messages, result.subscriptionCursor))
            }
        }
    }

    override fun cancel() {
        receiveMessagesRemoteAction.silentCancel()
    }
}
