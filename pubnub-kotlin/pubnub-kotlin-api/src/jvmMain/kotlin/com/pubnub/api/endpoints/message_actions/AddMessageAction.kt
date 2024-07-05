package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction

/**
 * @see [PubNub.addMessageAction]
 */
interface AddMessageAction : com.pubnub.kmp.endpoints.message_actions.AddMessageAction, Endpoint<PNAddMessageActionResult> {
    val channel: String
    val messageAction: PNMessageAction
}
