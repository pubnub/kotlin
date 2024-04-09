package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult

/**
 * @see [com.pubnub.api.PubNub.deleteMessages]
 */
interface DeleteMessages : Endpoint<PNDeleteMessagesResult> {
    val channels: List<String>
    val start: Long?
    val end: Long?
}
