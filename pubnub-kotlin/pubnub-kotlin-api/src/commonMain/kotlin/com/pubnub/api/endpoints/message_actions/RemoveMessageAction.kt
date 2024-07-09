package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.removeMessageAction]
 */
expect interface RemoveMessageAction : PNFuture<PNRemoveMessageActionResult>
