package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult

/**
 * @see [PubNub.addMessageAction]
 */
expect interface AddMessageAction : Endpoint<PNAddMessageActionResult> {
}