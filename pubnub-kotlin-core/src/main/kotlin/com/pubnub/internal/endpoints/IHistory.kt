package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.history.PNHistoryResult

interface IHistory : ExtendedRemoteAction<PNHistoryResult> {
    val channel: String
    val start: Long?
    val end: Long?
    val count: Int
    val reverse: Boolean
    val includeTimetoken: Boolean
    val includeMeta: Boolean
}