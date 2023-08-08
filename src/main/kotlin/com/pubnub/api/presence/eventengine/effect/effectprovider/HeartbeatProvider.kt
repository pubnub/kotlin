package com.pubnub.api.presence.eventengine.effect.effectprovider

import com.pubnub.api.endpoints.remoteaction.RemoteAction

fun interface HeartbeatProvider {
    fun getHeartbeatRemoteAction(channels: Set<String>, channelGroups: Set<String>): RemoteAction<Boolean>
}
