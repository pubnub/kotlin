package com.pubnub.api.endpoints.objects.uuid

import GetAllUUIDMetadataResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult

class GetAllUUIDMetadataImpl(pubnub: PubNub, params: PubNub.GetAllMetadataParameters) : GetAllUUIDMetadata,
    EndpointImpl<GetAllUUIDMetadataResponse, PNUUIDMetadataArrayResult>(promiseFactory = { pubnub.objects.getAllUUIDMetadata(params) },
        responseMapping = { pagedObjectsResponse: GetAllUUIDMetadataResponse ->
            PNUUIDMetadataArrayResult(
                pagedObjectsResponse.status.toInt(),
                pagedObjectsResponse.data.map(PubNub.UUIDMetadataObject::toPNUUIDMetadata),
                pagedObjectsResponse.totalCount?.toInt(),
                pagedObjectsResponse.next?.let { PNPage.PNNext(it) },
                pagedObjectsResponse.prev?.let { PNPage.PNPrev(it) },
            )
        }
    )
