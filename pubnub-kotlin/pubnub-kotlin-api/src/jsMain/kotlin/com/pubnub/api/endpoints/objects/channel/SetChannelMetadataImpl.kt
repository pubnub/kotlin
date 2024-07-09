package com.pubnub.api.endpoints.objects.channel

import PubNub
import SetChannelMetadataResponse
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.kmp.toChannelMetadataResult

class SetChannelMetadataImpl(pubnub: PubNub, params: PubNub.SetChannelMetadataParameters) : SetChannelMetadata,
    EndpointImpl<SetChannelMetadataResponse, PNChannelMetadataResult>(
        promiseFactory = { pubnub.objects.setChannelMetadata(params) },
        responseMapping = SetChannelMetadataResponse::toChannelMetadataResult
    )
