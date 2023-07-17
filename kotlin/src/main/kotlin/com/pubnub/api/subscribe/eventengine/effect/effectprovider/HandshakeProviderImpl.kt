package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

internal class HandshakeProviderImpl(val pubNub: PubNub) : HandshakeProvider {

    override fun getHandshakeRemoteAction(
        channels: List<String>,
        channelGroups: List<String>
    ): RemoteAction<SubscriptionCursor> {
        val subscribe = Subscribe(pubNub)
        subscribe.channels = channels
        subscribe.channelGroups = channelGroups
        subscribe.timetoken = 0
        subscribe.region = null
        return subscribe.map { SubscriptionCursor(it.metadata.timetoken, it.metadata.region) }
    }
}
