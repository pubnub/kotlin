package com.pubnub.kmp.endpoints.presence

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.presence.PNWhereNowResult

class WhereNowImpl(pubnub: PubNub, params: PubNub.WhereNowParameters) : WhereNow, EndpointImpl<PubNub.WhereNowResponse, PNWhereNowResult>(
    promiseFactory = { pubnub.whereNow(params) },
    responseMapping = { whereNowResponse ->
        PNWhereNowResult(
            whereNowResponse.channels.toList()
        )
    }
)