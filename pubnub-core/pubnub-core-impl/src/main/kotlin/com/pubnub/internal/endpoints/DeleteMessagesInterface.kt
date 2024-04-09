package com.pubnub.internal.endpoints

import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.internal.EndpointInterface

interface DeleteMessagesInterface : EndpointInterface<PNDeleteMessagesResult> {
    val channels: List<String>
    val start: Long?
    val end: Long?
}
