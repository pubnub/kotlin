package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult

/**
 * @see [PubNub.removeMessageAction]
 */
actual interface RemoveMessageAction : Endpoint<PNRemoveMessageActionResult> {
}