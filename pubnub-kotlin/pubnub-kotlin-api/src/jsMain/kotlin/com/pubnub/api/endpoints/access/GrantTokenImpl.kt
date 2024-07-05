package com.pubnub.kmp.endpoints.access

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult

class GrantTokenImpl(pubnub: PubNub, params: PubNub.GrantTokenParameters) : GrantToken, EndpointImpl<String, PNGrantTokenResult>(
    promiseFactory = { pubnub.grantToken(params) },
    responseMapping = {
        PNGrantTokenResult(it)
    }
)