package com.pubnub.api.endpoints.pubsub

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.PNPublishResult

class FireImpl(pubnub: PubNub, params: PubNub.FireParameters) : Publish, EndpointImpl<PubNub.PublishResponse, PNPublishResult>(
    promiseFactory = { pubnub.fire(params) },
    responseMapping = {
        PNPublishResult(it.timetoken.toLong())
    }
)
