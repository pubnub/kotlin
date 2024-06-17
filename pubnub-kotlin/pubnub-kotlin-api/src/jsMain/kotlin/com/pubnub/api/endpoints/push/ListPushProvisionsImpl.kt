package com.pubnub.api.endpoints.push

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult

class ListPushProvisionsImpl(pubnub: PubNub, params: PubNub.PushDeviceParameters) : ListPushProvisions, EndpointImpl<PubNub.PushListChannelsResponse, PNPushListProvisionsResult>(
    promiseFactory = { pubnub.push.listChannels(params) },
    responseMapping = {
        PNPushListProvisionsResult(it.channels.toList())
    }
)