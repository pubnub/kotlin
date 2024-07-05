package com.pubnub.kmp.endpoints.files

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult

class PublishFileMessageImpl(pubnub: PubNub, params: PubNub.PublishFileParameters) : PublishFileMessage, EndpointImpl<PubNub.PublishFileResponse, PNPublishFileMessageResult>(
    promiseFactory = { pubnub.publishFile(params) },
    responseMapping = {
        PNPublishFileMessageResult(it.timetoken.toLong())
    }
)