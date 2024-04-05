package com.pubnub.internal.endpoints.objects.channel

import com.pubnub.api.endpoints.objects.channel.GetChannelMetadata
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.getChannelMetadata]
 */
class GetChannelMetadataImpl internal constructor(getChannelMetadata: GetChannelMetadataInterface) :
    GetChannelMetadata,
    GetChannelMetadataInterface by getChannelMetadata,
    EndpointImpl<PNChannelMetadataResult>(getChannelMetadata)
