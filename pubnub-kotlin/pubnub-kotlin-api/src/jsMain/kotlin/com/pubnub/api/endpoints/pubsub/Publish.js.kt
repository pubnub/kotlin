package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.Endpoint
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.PNPublishResult
import PubNub as PubNubJs

/**
 * @see [PubNub.publish]
 */
actual interface Publish : Endpoint<PNPublishResult>

class PublishImpl(pubnub: PubNubJs, params: PubNub.PublishParameters) : Publish, EndpointImpl<PubNubJs.PublishResponse, PNPublishResult>(
    promiseFactory = { pubnub.publish(params) },
    responseMapping = {
        PNPublishResult(it.timetoken.toLong())
    }
)