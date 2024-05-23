package com.pubnub.api.endpoints.objects.channel

import GetChannelMetadataResponse
import GetUUIDMetadataResponse
import ObjectsResponse
import PubNub
import SetChannelMetadataResponse
import com.pubnub.api.EndpointImpl
import com.pubnub.api.endpoints.objects.uuid.GetUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.toPNUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.toPNUUIDMetadataResult
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.kmp.toMap

class GetChannelMetadataImpl(pubnub: PubNub, params: PubNub.GetChannelMetadataParameters) : GetChannelMetadata,
    EndpointImpl<GetChannelMetadataResponse, PNChannelMetadataResult>(promiseFactory = { pubnub.objects.getChannelMetadata(params) },
        responseMapping = SetChannelMetadataResponse::toChannelMetadataResult
    )
