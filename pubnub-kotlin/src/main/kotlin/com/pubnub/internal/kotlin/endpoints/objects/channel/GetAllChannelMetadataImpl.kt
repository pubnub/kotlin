package com.pubnub.internal.kotlin.endpoints.objects.channel

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.objects.channel.GetAllChannelMetadata
import com.pubnub.internal.endpoints.objects.channel.IGetAllChannelMetadata
import com.pubnub.internal.models.consumer.objects.channel.PNChannelMetadataArrayResult

/**
 * @see [PubNubImpl.getAllChannelMetadata]
 */
class GetAllChannelMetadataImpl internal constructor(getAllChannelMetadata: GetAllChannelMetadata) :
    DelegatingEndpoint<com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult, PNChannelMetadataArrayResult>(
        getAllChannelMetadata
    ),
    IGetAllChannelMetadata by getAllChannelMetadata,
    com.pubnub.api.endpoints.objects.channel.GetAllChannelMetadata {
    override fun convertAction(remoteAction: ExtendedRemoteAction<PNChannelMetadataArrayResult>): ExtendedRemoteAction<com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult> {
        return MappingRemoteAction(
            remoteAction,
            PNChannelMetadataArrayResult::toApi
        )
    }
}
