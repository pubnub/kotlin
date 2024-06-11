package com.pubnub.api.endpoints.message_actions

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult

/**
 * @see [PubNub.addMessageAction]
 */
expect interface AddMessageAction : PNFuture<PNAddMessageActionResult> {
}