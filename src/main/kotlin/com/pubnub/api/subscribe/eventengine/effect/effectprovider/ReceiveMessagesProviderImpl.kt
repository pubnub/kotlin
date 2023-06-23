package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.endpoints.remoteaction.ResultMappingWrapper.Companion.withMapping
import com.pubnub.api.managers.DuplicationManager
import com.pubnub.api.models.consumer.pubsub.PNEvent
import com.pubnub.api.subscribe.eventengine.effect.ReceiveMessagesResult
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.api.workers.SubscribeMessageProcessor

internal class ReceiveMessagesProviderImpl(val subscribe: Subscribe, val pubNub: PubNub) : ReceiveMessagesProvider {
    private val messageProcessor = SubscribeMessageProcessor(pubNub, DuplicationManager(pubNub.configuration))

    override fun getReceiveMessagesRemoteAction(
        channels: List<String>,
        channelGroups: List<String>,
        subscriptionCursor: SubscriptionCursor
    ): RemoteAction<ReceiveMessagesResult> {
        subscribe.channels = channels
        subscribe.channelGroups = channelGroups
        subscribe.timetoken = subscriptionCursor.timetoken
        subscribe.region = subscriptionCursor.region
        return subscribe.withMapping {
            val sdkMessages: List<PNEvent> = it.messages.mapNotNull {
                messageProcessor.processIncomingPayload(it)
            }
            ReceiveMessagesResult(
                sdkMessages, SubscriptionCursor(it.metadata.timetoken, it.metadata.region)
            )
        }
    }
}
