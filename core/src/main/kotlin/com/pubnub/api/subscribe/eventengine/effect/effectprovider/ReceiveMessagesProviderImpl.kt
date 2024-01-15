package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.effect.ReceiveMessagesResult
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.workers.SubscribeMessageProcessor

internal class ReceiveMessagesProviderImpl(val pubNub: PubNub, val messageProcessor: SubscribeMessageProcessor) : ReceiveMessagesProvider {

    override fun getReceiveMessagesRemoteAction(
        channels: Set<String>,
        channelGroups: Set<String>,
        subscriptionCursor: SubscriptionCursor
    ): RemoteAction<ReceiveMessagesResult> {
        val subscribe = Subscribe(pubNub)
        subscribe.channels = channels.toList()
        subscribe.channelGroups = channelGroups.toList()
        subscribe.timetoken = subscriptionCursor.timetoken
        subscribe.region = subscriptionCursor.region
        return subscribe.map {
            val sdkMessages: List<PNEvent> = it.messages.mapNotNull {
                messageProcessor.processIncomingPayload(it)
            }
            ReceiveMessagesResult(
                sdkMessages, SubscriptionCursor(it.metadata.timetoken, it.metadata.region)
            )
        }
    }
}
