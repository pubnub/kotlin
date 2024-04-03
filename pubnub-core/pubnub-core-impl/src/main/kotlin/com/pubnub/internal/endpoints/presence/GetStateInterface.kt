package com.pubnub.internal.endpoints.presence

import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.internal.EndpointInterface

interface GetStateInterface : EndpointInterface<PNGetStateResult> {
    val channels: List<String>
    val channelGroups: List<String>
    val uuid: String
}
