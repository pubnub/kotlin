package com.pubnub.kmp.endpoints.presence

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.JsonElementImpl
import com.pubnub.api.models.consumer.presence.PNSetStateResult

class SetStateImpl(pubnub: PubNub, params: PubNub.SetStateParameters) : SetState, EndpointImpl<PubNub.SetStateResponse, PNSetStateResult>(
    promiseFactory = { pubnub.setState(params) },
    responseMapping = { response ->
        PNSetStateResult(
            JsonElementImpl(response.state)
        )
    }
)