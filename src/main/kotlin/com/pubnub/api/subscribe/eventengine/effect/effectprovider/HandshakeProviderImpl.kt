package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

internal class HandshakeProviderImpl(val subscribe: Subscribe) : HandshakeProvider {

    override fun getHandshakeRemoteAction(
        channels: List<String>,
        channelGroups: List<String>
    ): RemoteAction<SubscriptionCursor> {
        subscribe.channels = channels
        subscribe.channelGroups = channelGroups
        return RemoteActionForHandshake(subscribe)
    }
}
