package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.pubsub.Subscribe
import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.endpoints.remoteaction.ResultMappingWrapper.Companion.withMapping
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

internal class HandshakeProviderImpl(val subscribe: Subscribe) : HandshakeProvider {

    override fun getHandshakeRemoteAction(
        channels: List<String>,
        channelGroups: List<String>
    ): RemoteAction<SubscriptionCursor> {
        subscribe.channels = channels
        subscribe.channelGroups = channelGroups
        subscribe.timetoken = 0
        subscribe.region = null
        return subscribe.withMapping { SubscriptionCursor(it.metadata.timetoken, it.metadata.region) }
    }
}
