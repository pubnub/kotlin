package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.core.RemoteAction

fun interface HandshakeProvider {
    fun getHandshakeRemoteAction(channels: List<String>, channelGroups: List<String>): RemoteAction<SubscriptionCursor>
}
