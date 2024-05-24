package com.pubnub.api.endpoints.objects.channel

import GetAllChannelMetadataResponse
import GetAllUUIDMetadataResponse
import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.endpoints.objects.uuid.GetAllUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.toPNUUIDMetadata
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult

class GetAllChannelMetadataImpl(pubnub: PubNub, params: PubNub.GetAllMetadataParameters) : GetAllChannelMetadata,
    EndpointImpl<GetAllChannelMetadataResponse, PNChannelMetadataArrayResult>(promiseFactory = { pubnub.objects.getAllChannelMetadata(params) },
        responseMapping = { pagedObjectsResponse: GetAllChannelMetadataResponse ->
            PNChannelMetadataArrayResult(
                pagedObjectsResponse.status.toInt(),
                pagedObjectsResponse.data.map(PubNub.ChannelMetadataObject::toChannelMetadata),
                pagedObjectsResponse.totalCount?.toInt(),
                pagedObjectsResponse.next?.let { PNPage.PNNext(it) },
                pagedObjectsResponse.prev?.let { PNPage.PNPrev(it) },
            )
        }
    )
