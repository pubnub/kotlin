package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.history.PNHistoryResult

/**
 * @see [PubNub.history]
 */
interface History : Endpoint<PNHistoryResult> {
    val channel: String
    val start: Long?
    val end: Long?
    val count: Int
    val reverse: Boolean
    val includeTimetoken: Boolean
    val includeMeta: Boolean
}
