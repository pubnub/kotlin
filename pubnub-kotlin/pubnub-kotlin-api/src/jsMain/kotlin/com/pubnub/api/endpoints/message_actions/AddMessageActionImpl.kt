package com.pubnub.kmp.endpoints.message_actions

import AddMessageActionResult
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.kmp.toMessageAction

class AddMessageActionImpl(pubnub: PubNub, params: PubNub.AddMessageActionParameters) : AddMessageAction, EndpointImpl<AddMessageActionResult, PNAddMessageActionResult>(
    promiseFactory = { pubnub.addMessageAction(params) },
    responseMapping = {
        PNAddMessageActionResult(
            it.data.toMessageAction()
        )
    }
)