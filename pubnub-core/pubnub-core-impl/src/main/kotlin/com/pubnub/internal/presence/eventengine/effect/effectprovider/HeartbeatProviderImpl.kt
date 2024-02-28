package com.pubnub.internal.presence.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.internal.CorePubNubClient
import com.pubnub.internal.endpoints.presence.HeartbeatEndpoint

internal class HeartbeatProviderImpl(val pubNub: CorePubNubClient) : HeartbeatProvider {
    override fun getHeartbeatRemoteAction(
        channels: Set<String>,
        channelGroups: Set<String>,
        state: Map<String, Any>?,
    ): RemoteAction<Boolean> {
        return HeartbeatEndpoint(
            pubNub,
            channels.toList(),
            channelGroups.toList(),
            state,
        )
    }
}
