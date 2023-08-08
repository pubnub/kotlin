package com.pubnub.api.presence.eventengine.effect.effectprovider

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.presence.Leave
import com.pubnub.api.endpoints.remoteaction.RemoteAction

internal class LeaveProviderImpl(val pubNub: PubNub) : LeaveProvider {
    override fun getLeaveRemoteAction(channels: Set<String>, channelGroups: Set<String>): RemoteAction<Boolean> {
        return Leave(pubNub).apply {
            this.channels = channels.toList()
            this.channelGroups = channelGroups.toList()
        }
    }
}
