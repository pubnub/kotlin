package com.pubnub.api.endpoints.message_actions

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult

/**
 * @see [PubNub.removeMessageAction]
 */
actual interface RemoveMessageAction : PNFuture<PNRemoveMessageActionResult>

