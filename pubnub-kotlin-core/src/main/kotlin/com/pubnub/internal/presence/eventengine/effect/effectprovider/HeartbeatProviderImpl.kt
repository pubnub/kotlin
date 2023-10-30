package com.pubnub.internal.presence.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.internal.BasePubNub.PubNubImpl
import com.pubnub.internal.endpoints.presence.Heartbeat

internal class HeartbeatProviderImpl(val pubNub: PubNubImpl) : HeartbeatProvider {
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