package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

internal fun interface HandshakeProvider {
    fun getHandshakeRemoteAction(channels: Set<String>, channelGroups: Set<String>, state: Map<String, Any>?): RemoteAction<SubscriptionCursor>
}
