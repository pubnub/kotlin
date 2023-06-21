package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.models.server.SubscribeEnvelope
import com.pubnub.api.subscribe.eventengine.effect.ReceiveMessagesResult
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.workers.SubscribeMessageProcessor

internal class RemoteActionForReceiveMessagesResult(
    private val subscribeRemoteAction: RemoteAction<SubscribeEnvelope>,
    private val messageProcessor: SubscribeMessageProcessor
) : RemoteAction<ReceiveMessagesResult> {

    override fun sync(): ReceiveMessagesResult? {
        val result = subscribeRemoteAction.sync()
        val sdkMessages: List<PNEvent> =
            result?.messages!!.mapNotNull { messageProcessor.processIncomingPayload(it) }
        return ReceiveMessagesResult(
            sdkMessages,
            SubscriptionCursor(result.metadata.timetoken, result.metadata.region)
        )
    }

    override fun silentCancel() {
        subscribeRemoteAction.silentCancel()
    }

    override fun async(callback: (result: ReceiveMessagesResult?, status: PNStatus) -> Unit) {
        subscribeRemoteAction.async { result: SubscribeEnvelope?, status ->
            val sdkMessages: List<PNEvent> =
                result?.messages!!.mapNotNull { messageProcessor.processIncomingPayload(it) }
            val receiveMessagesResult = ReceiveMessagesResult(
                sdkMessages,
                SubscriptionCursor(result.metadata.timetoken, result.metadata.region)
            )
            callback(receiveMessagesResult, status)
        }
    }
}
