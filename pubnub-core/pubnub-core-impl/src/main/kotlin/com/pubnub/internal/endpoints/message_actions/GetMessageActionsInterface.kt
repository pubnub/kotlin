package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.internal.EndpointInterface

interface GetMessageActionsInterface : EndpointInterface<PNGetMessageActionsResult> {
    val channel: String
    val page: PNBoundedPage
}
