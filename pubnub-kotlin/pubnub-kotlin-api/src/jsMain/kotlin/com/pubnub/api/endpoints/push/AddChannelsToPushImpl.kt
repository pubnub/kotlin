package com.pubnub.api.endpoints.push

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult

class AddChannelsToPushImpl(pubnub: PubNub, params: PubNub.PushChannelParameters) : AddChannelsToPush, EndpointImpl<Unit, PNPushAddChannelResult>(
    promiseFactory = { pubnub.push.addChannels(params) },
    responseMapping = {
        PNPushAddChannelResult()
    }
)