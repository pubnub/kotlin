package com.pubnub.api.endpoints.objects.channel

import GetUUIDMetadataResponse
import ObjectsResponse
import PubNub
import SetChannelMetadataResponse
import SetUUIDMetadataResponse
import com.pubnub.api.EndpointImpl
import com.pubnub.api.endpoints.objects.uuid.SetUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.toPNUUIDMetadataResult
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.kmp.toMap

class SetChannelMetadataImpl(pubnub: PubNub, params: PubNub.SetChannelMetadataParameters) : SetChannelMetadata,
    EndpointImpl<SetChannelMetadataResponse, PNChannelMetadataResult>(promiseFactory = { pubnub.objects.setChannelMetadata(params) },
        responseMapping = SetChannelMetadataResponse::toChannelMetadataResult
    )

internal fun SetChannelMetadataResponse.toChannelMetadataResult(): PNChannelMetadataResult {
    return PNChannelMetadataResult(status.toInt(),
        data.toChannelMetadata())
}

internal fun PubNub.ChannelMetadataObject.toChannelMetadata(): PNChannelMetadata {
    return PNChannelMetadata(
        id,
        name,
        description,
        custom?.toMap(),
        updated,
        eTag,
        type,
        status
    )
}
