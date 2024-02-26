package com.pubnub.internal.presence.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.internal.InternalPubNubClient
import com.pubnub.internal.endpoints.presence.Leave

internal class LeaveProviderImpl(val pubNub: InternalPubNubClient) : LeaveProvider {
    override fun getLeaveRemoteAction(
        channels: Set<String>,
        channelGroups: Set<String>,
    ): RemoteAction<Boolean> {
        return Leave(pubNub).apply {
            this.channels = channels.toList()
            this.channelGroups = channelGroups.toList()
        }
    }
}
