package com.pubnub.kmp.endpoints

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.PNTimeResult

class TimeImpl(pubnub: PubNub) : Time, EndpointImpl<PubNub.FetchTimeResponse, PNTimeResult>(
    promiseFactory = { pubnub.time() },
    responseMapping = { PNTimeResult(it.timetoken.toLong()) }
)