package com.pubnub.internal.kotlin.endpoints.objects.channel

import com.pubnub.api.endpoints.objects.channel.SetChannelMetadata
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.objects.channel.ISetChannelMetadata

/**
 * @see [PubNubImpl.setChannelMetadata]
 */
class SetChannelMetadataImpl internal constructor(setChannelMetadata: ISetChannelMetadata) :
    SetChannelMetadata, ISetChannelMetadata by setChannelMetadata
