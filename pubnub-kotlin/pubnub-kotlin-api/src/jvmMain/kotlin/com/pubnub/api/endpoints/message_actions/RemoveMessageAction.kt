package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.kmp.endpoints.message_actions.RemoveMessageAction

/**
 * @see [PubNub.removeMessageAction]
 */
interface RemoveMessageAction : RemoveMessageAction, Endpoint<PNRemoveMessageActionResult> {
    val channel: String
    val messageTimetoken: Long
    val actionTimetoken: Long
}
