package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.internal.EndpointInterface

interface AddMessageActionInterface : EndpointInterface<PNAddMessageActionResult> {
    val channel: String
    val messageAction: PNMessageAction
}
