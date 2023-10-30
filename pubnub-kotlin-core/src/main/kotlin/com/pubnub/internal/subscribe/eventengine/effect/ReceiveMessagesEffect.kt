package com.pubnub.internal.subscribe.eventengine.effect

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.v2.callbacks.onFailure
import com.pubnub.api.v2.callbacks.onSuccess
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
