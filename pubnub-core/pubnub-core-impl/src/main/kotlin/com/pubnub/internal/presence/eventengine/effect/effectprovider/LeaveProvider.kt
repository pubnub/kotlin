package com.pubnub.internal.presence.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction

internal fun interface LeaveProvider {
    fun getLeaveRemoteAction(
        channels: Set<String>,
        channelGroups: Set<String>,
    ): RemoteAction<Boolean>
}
