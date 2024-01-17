package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.objects.channel.GetChannelMetadata
import com.pubnub.internal.endpoints.objects.channel.IGetChannelMetadata

/**
 * @see [PubNub.getChannelMetadata]
 */
class GetChannelMetadata internal constructor(private val getChannelMetadata: GetChannelMetadata) :
    DelegatingEndpoint<PNChannelMetadataResult>(), IGetChannelMetadata by getChannelMetadata {
    override fun createAction(): Endpoint<PNChannelMetadataResult> = getChannelMetadata.mapIdentity()
}