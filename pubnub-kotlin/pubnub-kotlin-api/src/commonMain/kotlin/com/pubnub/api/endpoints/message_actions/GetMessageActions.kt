package com.pubnub.api.endpoints.message_actions

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult

/**
 * @see [PubNub.getMessageActions]
 */
expect interface GetMessageActions : PNFuture<PNGetMessageActionsResult> {
}