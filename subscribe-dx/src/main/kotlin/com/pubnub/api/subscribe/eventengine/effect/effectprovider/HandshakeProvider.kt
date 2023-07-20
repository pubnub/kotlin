package com.pubnub.api.subscribe.eventengine.effect.effectprovider

import com.pubnub.api.subscribe.eventengine.event.SubscriptionCursor
import com.pubnub.core.CoreRemoteAction
import com.pubnub.core.Status

fun interface HandshakeProvider {
    fun getHandshakeRemoteAction(channels: List<String>, channelGroups: List<String>): CoreRemoteAction<SubscriptionCursor, Status>
}
