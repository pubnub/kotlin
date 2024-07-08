package com.pubnub.api.endpoints.objects.uuid

import GetUUIDMetadataResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.kmp.toPNUUIDMetadataResult

class GetUUIDMetadataImpl(pubnub: PubNub, params: PubNub.GetUUIDMetadataParameters) : GetUUIDMetadata,
    EndpointImpl<GetUUIDMetadataResponse, PNUUIDMetadataResult>(
        promiseFactory = { pubnub.objects.getUUIDMetadata(params) },
        responseMapping = GetUUIDMetadataResponse::toPNUUIDMetadataResult
    )
