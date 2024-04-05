package com.pubnub.internal.endpoints.objects.channel

import com.pubnub.api.endpoints.objects.channel.SetChannelMetadata
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.setChannelMetadata]
 */
class SetChannelMetadataImpl internal constructor(setChannelMetadata: SetChannelMetadataInterface) :
    SetChannelMetadata,
    SetChannelMetadataInterface by setChannelMetadata,
    EndpointImpl<PNChannelMetadataResult>(setChannelMetadata)
