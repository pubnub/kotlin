package com.pubnub.api.endpoints.pubsub

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.PNPublishResult

/**
 * @see [PubNub.signal]
 */
actual interface Signal : Endpoint<PNPublishResult>

class SignalImpl(pubnub: PubNub, params: PubNub.SignalParameters) : Signal, EndpointImpl<PubNub.SignalResponse, PNPublishResult>(
    promiseFactory = { pubnub.signal(params) },
    responseMapping = {
        PNPublishResult(it.timetoken.toLong())
    }
)