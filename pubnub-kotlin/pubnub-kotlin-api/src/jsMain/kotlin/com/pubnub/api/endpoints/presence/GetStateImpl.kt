package com.pubnub.api.endpoints.presence

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.JsonElementImpl
import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.kmp.JsMap
import com.pubnub.kmp.toMap

class GetStateImpl(pubnub: PubNub, params: PubNub.GetStateParameters) : GetState, EndpointImpl<PubNub.GetStateResponse, PNGetStateResult>(
    promiseFactory = { pubnub.getState(params) },
    responseMapping = { response ->
        PNGetStateResult(
            response.channels.unsafeCast<JsMap<Any?>>().toMap().mapValues {
                JsonElementImpl(it.value)
            }
        )
    }
)