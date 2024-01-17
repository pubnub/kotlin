package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.objects.channel.GetAllChannelMetadata
import com.pubnub.internal.endpoints.objects.channel.IGetAllChannelMetadata

/**
 * @see [PubNub.getAllChannelMetadata]
 */
class GetAllChannelMetadata internal constructor(private val getAllChannelMetadata: GetAllChannelMetadata) :
    DelegatingEndpoint<PNChannelMetadataArrayResult>(), IGetAllChannelMetadata by getAllChannelMetadata {

    override fun createAction(): Endpoint<PNChannelMetadataArrayResult> =
        getAllChannelMetadata.map(PNChannelMetadataArrayResult::from)

}