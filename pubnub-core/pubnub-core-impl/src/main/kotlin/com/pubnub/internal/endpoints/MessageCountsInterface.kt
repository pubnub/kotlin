package com.pubnub.internal.endpoints

import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.internal.EndpointInterface

interface MessageCountsInterface : EndpointInterface<PNMessageCountResult> {
    val channels: List<String>
    val channelsTimetoken: List<Long>
}
