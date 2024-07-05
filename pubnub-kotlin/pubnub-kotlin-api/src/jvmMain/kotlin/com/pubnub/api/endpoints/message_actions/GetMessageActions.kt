package com.pubnub.api.endpoints.message_actions

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult

/**
 * @see [PubNub.getMessageActions]
 */
interface GetMessageActions : com.pubnub.kmp.endpoints.message_actions.GetMessageActions, Endpoint<PNGetMessageActionsResult> {
    val channel: String
    val page: PNBoundedPage
}
