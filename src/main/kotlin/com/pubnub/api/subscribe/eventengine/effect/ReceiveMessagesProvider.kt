package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

fun interface ReceiveMessagesProvider {
    fun getRemoteActionForReceiveMessages(
        channels: List<String>,
        channelGroups: List<String>,
        subscriptionCursor: SubscriptionCursor
    ): RemoteAction<ReceiveMessagesResult>
}
