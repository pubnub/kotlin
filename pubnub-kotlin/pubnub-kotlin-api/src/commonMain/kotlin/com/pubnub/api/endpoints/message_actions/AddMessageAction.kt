package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.addMessageAction]
 */
expect interface AddMessageAction : PNFuture<PNAddMessageActionResult>
