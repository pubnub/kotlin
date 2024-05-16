package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.EndpointImpl
import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.PNPublishResult

/**
 * @see [PubNub.publish]
 */
actual interface Publish : Endpoint<PNPublishResult>

class PublishImpl(pubnub: PubNub, params: PubNub.PublishParameters) : Publish, EndpointImpl<PubNub.PublishResponse, PNPublishResult>(
    promiseFactory = { pubnub.publish(params) },
    responseMapping = {
        PNPublishResult(it.timetoken.toLong())
    }
)

class FireImpl(pubnub: PubNub, params: PubNub.FireParameters) : Publish, EndpointImpl<PubNub.PublishResponse, PNPublishResult>(
    promiseFactory = { pubnub.fire(params) },
    responseMapping = {
        PNPublishResult(it.timetoken.toLong())
    }
)
