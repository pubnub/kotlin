package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

internal class HandshakeProviderImpl(val pubNub: PubNub) : HandshakeProvider {

    override fun getHandshakeRemoteAction(
        channels: Set<String>,
        channelGroups: Set<String>,
        state: Map<String, Any>?,
    ): RemoteAction<SubscriptionCursor> {
        val subscribe = Subscribe(pubNub)
        subscribe.channels = channels.toList()
        subscribe.channelGroups = channelGroups.toList()
        subscribe.timetoken = 0
        subscribe.region = null
        subscribe.state = state
        return subscribe.map { SubscriptionCursor(it.metadata.timetoken, it.metadata.region) }
    }
}
