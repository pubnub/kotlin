package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.Endpoint
import com.pubnub.internal.PubNubImpl
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.internal.endpoints.objects.channel.ISetChannelMetadata

/**
 * @see [PubNubImpl.setChannelMetadata]
 */
class SetChannelMetadata internal constructor(setChannelMetadata: ISetChannelMetadata) :
    Endpoint<PNChannelMetadataResult>(), ISetChannelMetadata by setChannelMetadata
