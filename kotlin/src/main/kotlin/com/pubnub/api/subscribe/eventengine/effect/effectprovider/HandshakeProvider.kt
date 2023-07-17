package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor

fun interface HandshakeProvider {
    fun getHandshakeRemoteAction(channels: List<String>, channelGroups: List<String>): RemoteAction<SubscriptionCursor>
}
