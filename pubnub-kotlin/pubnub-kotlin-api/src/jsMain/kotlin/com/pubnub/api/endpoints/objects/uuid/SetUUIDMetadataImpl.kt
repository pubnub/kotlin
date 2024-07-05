package com.pubnub.kmp.endpoints.objects.uuid

import GetUUIDMetadataResponse
import PubNub
import SetUUIDMetadataResponse
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.kmp.toPNUUIDMetadataResult

class SetUUIDMetadataImpl(pubnub: PubNub, params: PubNub.SetUUIDMetadataParameters) : SetUUIDMetadata,
    EndpointImpl<SetUUIDMetadataResponse, PNUUIDMetadataResult>(promiseFactory = { pubnub.objects.setUUIDMetadata(params) },
        responseMapping = GetUUIDMetadataResponse::toPNUUIDMetadataResult
    )