package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.eventengine.Sink
import com.pubnub.api.subscribe.eventengine.event.SubscribeEvent
import org.slf4j.LoggerFactory

internal class ReceiveMessagesEffect(
    private val receiveMessagesRemoteAction: RemoteAction<ReceiveMessagesResult>,
    private val subscribeEventSink: Sink<SubscribeEvent>,
) : ManagedEffect {
    private val log = LoggerFactory.getLogger(ReceiveMessagesEffect::class.java)

    override fun runEffect() {
        log.trace("Running ReceiveMessagesEffect")

        receiveMessagesRemoteAction.async { result ->
            result.onFailure {
                subscribeEventSink.add(
                    SubscribeEvent.ReceiveFailure(
                        PubNubException.from(it)
                    )
                )
            }.onSuccess {
                subscribeEventSink.add(SubscribeEvent.ReceiveSuccess(it.messages, it.subscriptionCursor))
            }
        }
    }

    override fun cancel() {
        receiveMessagesRemoteAction.silentCancel()
    }
}
