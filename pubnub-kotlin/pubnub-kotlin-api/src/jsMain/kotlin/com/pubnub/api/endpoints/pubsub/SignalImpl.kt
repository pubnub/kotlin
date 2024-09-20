package com.pubnub.api.endpoints.pubsub

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.PNPublishResult

class SignalImpl(pubnub: PubNub, params: PubNub.SignalParameters) : Signal, EndpointImpl<PubNub.SignalResponse, PNPublishResult>(
    promiseFactory = { pubnub.signal(params) },
    responseMapping = {
        PNPublishResult(it.timetoken.toLong())
    }
)
