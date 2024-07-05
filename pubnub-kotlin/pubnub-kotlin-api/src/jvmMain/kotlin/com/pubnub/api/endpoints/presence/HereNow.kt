package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.presence.PNHereNowResult

/**
 * @see [PubNub.hereNow]
 */
interface HereNow : com.pubnub.kmp.endpoints.presence.HereNow, Endpoint<PNHereNowResult> {
    val channels: List<String>
    val channelGroups: List<String>
    val includeState: Boolean
    val includeUUIDs: Boolean
}
