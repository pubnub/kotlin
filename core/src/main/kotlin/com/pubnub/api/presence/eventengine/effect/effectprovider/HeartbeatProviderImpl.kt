package com.pubnub.api.presence.eventengine.effect.effectprovider

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.presence.Heartbeat
import com.pubnub.api.endpoints.remoteaction.RemoteAction

internal class HeartbeatProviderImpl(val pubNub: PubNub) : HeartbeatProvider {
    override fun getHeartbeatRemoteAction(
        channels: Set<String>,
        channelGroups: Set<String>,
        state: Map<String, Any>?
    ): RemoteAction<Boolean> {
        return Heartbeat(
            pubNub,
            channels.toList(),
            channelGroups.toList(),
            state
        )
    }
}
