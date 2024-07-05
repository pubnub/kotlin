package com.pubnub.kmp.endpoints.objects.channel

import PubNub
import RemoveChannelMetadataResponse
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult

class RemoveChannelMetadataImpl(pubnub: PubNub, params: PubNub.RemoveChannelMetadataParameters) : RemoveChannelMetadata,
    EndpointImpl<RemoveChannelMetadataResponse, PNRemoveMetadataResult>(promiseFactory = { pubnub.objects.removeChannelMetadata(params) },
        responseMapping = {
            PNRemoveMetadataResult(it.status.toInt())
        })