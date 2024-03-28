package com.pubnub.internal.presence.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.endpoints.presence.LeaveEndpoint

internal class LeaveProviderImpl(val pubNub: PubNubCore) : LeaveProvider {
    override fun getLeaveRemoteAction(
        channels: Set<String>,
        channelGroups: Set<String>,
    ): RemoteAction<Boolean> {
        return LeaveEndpoint(pubNub).apply {
            this.channels = channels.toList()
            this.channelGroups = channelGroups.toList()
        }
    }
}
