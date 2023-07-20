package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.subscribe.eventengine.effect.ReceiveMessagesResult
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.core.RemoteAction
import com.pubnub.core.Status

fun interface ReceiveMessagesProvider {
    fun getReceiveMessagesRemoteAction(
        channels: List<String>,
        channelGroups: List<String>,
        subscriptionCursor: SubscriptionCursor
    ): RemoteAction<ReceiveMessagesResult, Status>
}
