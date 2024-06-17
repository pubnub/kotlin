package com.pubnub.api.endpoints.push

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult

class RemoveAllPushChannelsForDeviceImpl(pubnub: PubNub, params: PubNub.PushDeviceParameters) : RemoveAllPushChannelsForDevice, EndpointImpl<Unit, PNPushRemoveAllChannelsResult>(
    promiseFactory = { pubnub.push.deleteDevice(params) },
    responseMapping = {
        PNPushRemoveAllChannelsResult()
    }
)