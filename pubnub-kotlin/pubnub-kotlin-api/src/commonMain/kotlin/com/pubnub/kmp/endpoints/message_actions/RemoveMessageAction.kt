package com.pubnub.kmp.endpoints.message_actions

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult

/**
 * @see [PubNub.removeMessageAction]
 */
interface RemoveMessageAction : PNFuture<PNRemoveMessageActionResult> {
}