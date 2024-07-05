package com.pubnub.kmp.endpoints.message_actions

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult

/**
 * @see [PubNub.addMessageAction]
 */
interface AddMessageAction : PNFuture<PNAddMessageActionResult> {
}