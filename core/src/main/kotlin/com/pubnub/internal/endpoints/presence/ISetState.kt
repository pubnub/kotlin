package com.pubnub.internal.endpoints.presence

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.presence.PNSetStateResult

interface ISetState : ExtendedRemoteAction<PNSetStateResult> {
    val channels: List<String>
    val channelGroups: List<String>
    val state: Any
    val uuid: String
}
