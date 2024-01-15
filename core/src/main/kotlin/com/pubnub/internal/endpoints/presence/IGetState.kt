package com.pubnub.internal.endpoints.presence

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.presence.PNGetStateResult

interface IGetState : ExtendedRemoteAction<PNGetStateResult> {
    val channels: List<String>
    val channelGroups: List<String>
    val uuid: String
}
