package com.pubnub.internal.endpoints.presence

import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.internal.EndpointInterface

interface SetStateInterface : EndpointInterface<PNSetStateResult> {
    val channels: List<String>
    val channelGroups: List<String>
    val state: Any
    val uuid: String
}
