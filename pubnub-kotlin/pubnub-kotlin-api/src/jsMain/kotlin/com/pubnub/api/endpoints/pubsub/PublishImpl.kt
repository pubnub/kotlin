package com.pubnub.api.endpoints.pubsub

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.PNPublishResult

class PublishImpl(pubnub: PubNub, params: PubNub.PublishParameters) : Publish, EndpointImpl<PubNub.PublishResponse, PNPublishResult>(
    promiseFactory = { pubnub.publish(params) },
    responseMapping = {
        PNPublishResult(it.timetoken.toLong())
    }
)