package com.pubnub.internal.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.pubsub.SubscribeEndpoint
import com.pubnub.internal.subscribe.eventengine.event.SubscriptionCursor

internal class HandshakeProviderImpl(val pubNub: PubNubImpl) : HandshakeProvider {
    override fun getHandshakeRemoteAction(
        channels: Set<String>,
        channelGroups: Set<String>,
        state: Map<String, Any>?,
    ): RemoteAction<SubscriptionCursor> {
        val subscribe = SubscribeEndpoint(pubNub)
        subscribe.channels = channels.toList()
        subscribe.channelGroups = channelGroups.toList()
        subscribe.timetoken = 0
        subscribe.region = null
        subscribe.state = state
        return subscribe.map { SubscriptionCursor(it.metadata.timetoken, it.metadata.region) }
    }
}
