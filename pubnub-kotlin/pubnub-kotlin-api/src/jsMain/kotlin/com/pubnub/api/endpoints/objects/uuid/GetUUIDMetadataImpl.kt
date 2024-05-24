package com.pubnub.api.endpoints.objects.uuid

import GetUUIDMetadataResponse
import ObjectsResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.kmp.toMap

class GetUUIDMetadataImpl(pubnub: PubNub, params: PubNub.GetUUIDMetadataParameters) : GetUUIDMetadata,
    EndpointImpl<GetUUIDMetadataResponse, PNUUIDMetadataResult>(promiseFactory = { pubnub.objects.getUUIDMetadata(params) },
        responseMapping = GetUUIDMetadataResponse::toPNUUIDMetadataResult
    )


fun ObjectsResponse<PubNub.UUIDMetadataObject>.toPNUUIDMetadataResult() = PNUUIDMetadataResult(status.toInt(), data.toPNUUIDMetadata())

internal fun PubNub.UUIDMetadataObject.toPNUUIDMetadata() = PNUUIDMetadata(
    id, name, externalId, profileUrl, email, custom?.toMap(), updated, eTag, type, status
)