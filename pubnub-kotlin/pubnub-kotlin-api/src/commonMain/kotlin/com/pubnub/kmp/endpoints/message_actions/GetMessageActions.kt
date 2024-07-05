package com.pubnub.kmp.endpoints.message_actions

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult

/**
 * @see [PubNub.getMessageActions]
 */
interface GetMessageActions : PNFuture<PNGetMessageActionsResult> {
}