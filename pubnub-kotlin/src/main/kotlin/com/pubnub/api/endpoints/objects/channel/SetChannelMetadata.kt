package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.objects.channel.ISetChannelMetadata
import com.pubnub.internal.endpoints.objects.channel.SetChannelMetadata

/**
 * @see [PubNub.setChannelMetadata]
 */
class SetChannelMetadata internal constructor(private val setChannelMetadata: SetChannelMetadata) :
    DelegatingEndpoint<PNChannelMetadataResult>(), ISetChannelMetadata by setChannelMetadata {
    override fun createAction(): Endpoint<PNChannelMetadataResult> = setChannelMetadata.mapIdentity()
}
