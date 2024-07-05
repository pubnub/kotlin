package com.pubnub.kmp.endpoints.objects.channel

import GetChannelMetadataResponse
import PubNub
import SetChannelMetadataResponse
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.kmp.toChannelMetadataResult

class GetChannelMetadataImpl(pubnub: PubNub, params: PubNub.GetChannelMetadataParameters) : GetChannelMetadata,
    EndpointImpl<GetChannelMetadataResponse, PNChannelMetadataResult>(promiseFactory = { pubnub.objects.getChannelMetadata(params) },
        responseMapping = SetChannelMetadataResponse::toChannelMetadataResult
    )
