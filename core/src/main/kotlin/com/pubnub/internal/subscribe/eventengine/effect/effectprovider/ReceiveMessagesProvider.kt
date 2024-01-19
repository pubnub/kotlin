package com.pubnub.internal.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.internal.subscribe.eventengine.effect.ReceiveMessagesResult
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor

internal fun interface ReceiveMessagesProvider {
    fun getReceiveMessagesRemoteAction(
        channels: Set<String>,
        channelGroups: Set<String>,
        subscriptionCursor: SubscriptionCursor
    ): RemoteAction<ReceiveMessagesResult>
}
