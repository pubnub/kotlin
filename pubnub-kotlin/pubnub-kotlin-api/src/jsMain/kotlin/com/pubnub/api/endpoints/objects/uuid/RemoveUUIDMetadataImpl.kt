package com.pubnub.api.endpoints.objects.uuid

import PubNub
import RemoveUUIDMetadataResponse
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult

class RemoveUUIDMetadataImpl(pubnub: PubNub, params: PubNub.RemoveUUIDMetadataParameters) : RemoveUUIDMetadata,
    EndpointImpl<RemoveUUIDMetadataResponse, PNRemoveMetadataResult>(
        promiseFactory = { pubnub.objects.removeUUIDMetadata(params) },
        responseMapping = {
            PNRemoveMetadataResult(it.status.toInt())
        }
    )
