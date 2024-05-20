package com.pubnub.api.endpoints.presence

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.JsonValueImpl
import com.pubnub.api.models.consumer.presence.PNSetStateResult

class SetStateImpl(pubnub: PubNub, params: PubNub.SetStateParameters) : SetState, EndpointImpl<PubNub.SetStateResponse, PNSetStateResult>(
    promiseFactory = { pubnub.setState(params) },
    responseMapping = { response ->
        PNSetStateResult(
            JsonValueImpl(response.state)
        )
    }
)