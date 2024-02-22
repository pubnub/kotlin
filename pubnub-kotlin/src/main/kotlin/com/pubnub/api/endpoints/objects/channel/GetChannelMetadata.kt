package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.Endpoint
import com.pubnub.internal.PubNubImpl
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.internal.endpoints.objects.channel.IGetChannelMetadata

/**
 * @see [PubNubImpl.getChannelMetadata]
 */
class GetChannelMetadata internal constructor(getChannelMetadata: IGetChannelMetadata) :
    Endpoint<PNChannelMetadataResult>(), IGetChannelMetadata by getChannelMetadata
