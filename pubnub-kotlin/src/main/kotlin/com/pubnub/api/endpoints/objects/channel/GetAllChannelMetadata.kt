package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.internal.endpoints.objects.channel.GetAllChannelMetadata
import com.pubnub.internal.endpoints.objects.channel.IGetAllChannelMetadata
import com.pubnub.internal.models.consumer.objects.channel.PNChannelMetadataArrayResult

/**
 * @see [PubNub.getAllChannelMetadata]
 */
class GetAllChannelMetadata internal constructor(getAllChannelMetadata: GetAllChannelMetadata) :
    DelegatingEndpoint<com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult, PNChannelMetadataArrayResult>(
        getAllChannelMetadata
    ),
    IGetAllChannelMetadata by getAllChannelMetadata {
    override fun convertAction(remoteAction: ExtendedRemoteAction<PNChannelMetadataArrayResult>): ExtendedRemoteAction<com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult> {
        return MappingRemoteAction(
            remoteAction,
            com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult::from
        )
    }
}
