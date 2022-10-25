package com.pubnub.api.presence.internal

import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.presence.Heartbeat
import com.pubnub.api.endpoints.presence.Leave
import com.pubnub.api.endpoints.presence.SetState
import com.pubnub.api.endpoints.remoteaction.Cancelable
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.presence.PNSetStateResult

internal fun PubNub.iAmHere(
    channels: List<String>,
    channelGroups: List<String>,
    callback: (result: Boolean?, status: PNStatus) -> Unit
): Cancelable {
    return Heartbeat(
        this,
        channels = channels,
        channelGroups = channelGroups
    ).also {
        it.async(callback)
    }
}

internal fun PubNub.iAmAway(
    channels: List<String>,
    channelGroups: List<String>,
    callback: (result: Boolean?, status: PNStatus) -> Unit
): Cancelable {
    return Leave(this).also {
        it.channels = channels
        it.channelGroups = channelGroups
        it.async(callback)
    }
}

internal fun PubNub.setPresenceState(
    channels: List<String>,
    channelGroups: List<String>,
    state: Any,
    callback: (result: PNSetStateResult?, status: PNStatus) -> Unit
): Cancelable {
    return SetState(
        pubnub = this,
        channels = channels,
        channelGroups = channelGroups,
        state = state,
        uuid = this.configuration.userId.value
    ).also {
        it.async(callback)
    }
}
