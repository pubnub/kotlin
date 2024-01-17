package com.pubnub.internal.endpoints.message_actions

import com.pubnub.api.models.consumer.message_actions.PNMessageAction

interface IAddMessageAction {
    val channel: String
    val messageAction: PNMessageAction
}
