package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.internal.EndpointInterface

interface RemoveMessageActionInterface : EndpointInterface<PNRemoveMessageActionResult> {
    val channel: String
    val messageTimetoken: Long
    val actionTimetoken: Long
}
