package com.pubnub.internal.endpoints.objects.channel

import com.pubnub.api.endpoints.objects.channel.SetChannelMetadata
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.setChannelMetadata]
 */
class SetChannelMetadataImpl internal constructor(setChannelMetadata: SetChannelMetadataInterface) :
    SetChannelMetadata, SetChannelMetadataInterface by setChannelMetadata
