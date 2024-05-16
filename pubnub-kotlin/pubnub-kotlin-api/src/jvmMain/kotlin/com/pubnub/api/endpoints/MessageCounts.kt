package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.history.PNMessageCountResult

/**
 * @see [PubNub.messageCounts]
 */
actual interface MessageCounts : Endpoint<PNMessageCountResult> {
    val channels: List<String>
    val channelsTimetoken: List<Long>
}
