package com.pubnub.api.presence.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction

internal fun interface HeartbeatProvider {
    fun getHeartbeatRemoteAction(channels: Set<String>, channelGroups: Set<String>, state: Any?): RemoteAction<Boolean>
}
