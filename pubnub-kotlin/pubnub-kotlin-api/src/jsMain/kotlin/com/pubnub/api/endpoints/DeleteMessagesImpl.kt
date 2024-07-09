package com.pubnub.api.endpoints

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult

class DeleteMessagesImpl(pubnub: PubNub, params: PubNub.DeleteMessagesParameters) : DeleteMessages,
    EndpointImpl<Unit, PNDeleteMessagesResult>(
        promiseFactory = { pubnub.deleteMessages(params) },
        responseMapping = {
            PNDeleteMessagesResult()
        }
    )
