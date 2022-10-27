package com.pubnub.api.models.server.message_actions

import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.message_actions.PNMessageAction

data class MessageActionsResponse(
    val status: Int,
    val data: List<PNMessageAction> = listOf(),
    val more: PNBoundedPage
)
