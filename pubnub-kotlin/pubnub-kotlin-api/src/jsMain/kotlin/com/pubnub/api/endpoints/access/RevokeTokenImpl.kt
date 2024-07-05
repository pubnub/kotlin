package com.pubnub.kmp.endpoints.access

import PubNub
import com.pubnub.api.EndpointImpl

class RevokeTokenImpl(pubnub: PubNub, params: String) : RevokeToken, EndpointImpl<PubNub.RevokeTokenResponse, Unit>(
    promiseFactory = { pubnub.revokeToken(params) },
    responseMapping = { }
)