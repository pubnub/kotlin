package com.pubnub.internal.endpoints

import com.pubnub.api.models.consumer.history.PNHistoryResult
import com.pubnub.internal.EndpointInterface

interface HistoryInterface : EndpointInterface<PNHistoryResult> {
    val channel: String
    val start: Long?
    val end: Long?
    val count: Int
    val reverse: Boolean
    val includeTimetoken: Boolean
    val includeMeta: Boolean
}
