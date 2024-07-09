package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.getMessageActions]
 */
expect interface GetMessageActions : PNFuture<PNGetMessageActionsResult>
