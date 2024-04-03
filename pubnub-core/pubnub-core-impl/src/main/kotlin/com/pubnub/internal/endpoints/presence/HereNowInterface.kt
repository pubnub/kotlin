package com.pubnub.internal.endpoints.presence

import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.internal.EndpointInterface

interface HereNowInterface : EndpointInterface<PNHereNowResult> {
    val channels: List<String>
    val channelGroups: List<String>
    val includeState: Boolean
    val includeUUIDs: Boolean
}
