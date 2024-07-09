package com.pubnub.api.endpoints.push

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult

class RemoveChannelsFromPushImpl(pubnub: PubNub, params: PubNub.PushChannelParameters) : RemoveChannelsFromPush, EndpointImpl<Unit, PNPushRemoveChannelResult>(
    promiseFactory = { pubnub.push.removeChannels(params) },
    responseMapping = {
        PNPushRemoveChannelResult()
    }
)
