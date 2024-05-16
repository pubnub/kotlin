package com.pubnub.api.endpoints.message_actions

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult

/**
 * @see [PubNub.addMessageAction]
 */
actual interface AddMessageAction : Endpoint<PNAddMessageActionResult>

