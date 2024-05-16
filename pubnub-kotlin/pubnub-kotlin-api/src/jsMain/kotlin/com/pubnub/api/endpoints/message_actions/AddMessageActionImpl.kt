package com.pubnub.api.endpoints.message_actions

import AddMessageActionResult
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction

class AddMessageActionImpl(pubnub: PubNub, params: PubNub.AddMessageActionParameters) : AddMessageAction, EndpointImpl<AddMessageActionResult, PNAddMessageActionResult>(
    promiseFactory = { pubnub.addMessageAction(params) },
    responseMapping = {
        PNAddMessageActionResult(
            PNMessageAction(
                it.data.type,
                it.data.value,
                it.data.messageTimetoken.toLong()
            ).apply {
                actionTimetoken = it.data.actionTimetoken.toLong()
            }
        )
    }
)