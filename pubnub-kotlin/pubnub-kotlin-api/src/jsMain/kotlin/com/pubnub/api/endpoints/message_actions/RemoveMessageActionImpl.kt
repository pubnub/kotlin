package com.pubnub.api.endpoints.message_actions

import PubNub
import RemoveMessageActionResult
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult

class RemoveMessageActionImpl(pubnub: PubNub, params: PubNub.RemoveMessageActionParameters) : RemoveMessageAction, EndpointImpl<RemoveMessageActionResult, PNRemoveMessageActionResult>(
    promiseFactory = { pubnub.removeMessageAction(params) },
    responseMapping = {
        PNRemoveMessageActionResult()
    }
)
