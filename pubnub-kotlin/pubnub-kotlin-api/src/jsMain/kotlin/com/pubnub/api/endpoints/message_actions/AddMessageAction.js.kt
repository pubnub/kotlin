package com.pubnub.api.endpoints.message_actions

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult

/**
 * @see [PubNub.addMessageAction]
 */
actual interface AddMessageAction : PNFuture<PNAddMessageActionResult>

