package com.pubnub.api.endpoints.objects.uuid

import PubNub
import RemoveUUIDMetadataResponse
import SetUUIDMetadataResponse
import com.pubnub.api.Endpoint
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult

actual interface RemoveUUIDMetadata : Endpoint<PNRemoveMetadataResult>

class RemoveUUIDMetadataImpl(pubnub: PubNub, params: PubNub.RemoveUUIDMetadataParameters) : RemoveUUIDMetadata,
    EndpointImpl<RemoveUUIDMetadataResponse, PNRemoveMetadataResult>(promiseFactory = { pubnub.objects.removeUUIDMetadata(params) },
        responseMapping = {
            PNRemoveMetadataResult(it.status.toInt())
        })