package com.pubnub.api.endpoints.objects.uuid

import PubNub
import SetUUIDMetadataResponse
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.kmp.toMap

class SetUUIDMetadataImpl(pubnub: PubNub, params: PubNub.SetUUIDMetadataParameters) : SetUUIDMetadata,
    EndpointImpl<SetUUIDMetadataResponse, PNUUIDMetadataResult>(promiseFactory = { pubnub.objects.setUUIDMetadata(params) },
        responseMapping = {
            PNUUIDMetadataResult(it.status.toInt(), with(it.data) {
                PNUUIDMetadata(
                    id, name, externalId, profileUrl, email, custom?.toMap(), updated, eTag, type, status
                )
            })
        })