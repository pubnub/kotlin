package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult

interface IDeleteMessages : ExtendedRemoteAction<PNDeleteMessagesResult> {
    val channels: List<String>
    val start: Long?
    val end: Long?
}
