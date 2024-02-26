package com.pubnub.internal.endpoints.objects.channel

import com.pubnub.api.endpoints.objects.channel.GetChannelMetadata
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.getChannelMetadata]
 */
class GetChannelMetadataImpl internal constructor(getChannelMetadata: IGetChannelMetadata) :
    GetChannelMetadata, IGetChannelMetadata by getChannelMetadata
