package com.pubnub.api.endpoints.objects.channel

import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.internal.endpoints.objects.channel.IRemoveChannelMetadata
import com.pubnub.internal.endpoints.objects.channel.RemoveChannelMetadata

class RemoveChannelMetadata internal constructor(removeChannelMetadata: RemoveChannelMetadata) :
    DelegatingEndpoint<PNRemoveMetadataResult, com.pubnub.internal.models.consumer.objects.PNRemoveMetadataResult>(
        removeChannelMetadata
    ), IRemoveChannelMetadata by removeChannelMetadata {
    override fun convertAction(remoteAction: ExtendedRemoteAction<com.pubnub.internal.models.consumer.objects.PNRemoveMetadataResult>): ExtendedRemoteAction<PNRemoveMetadataResult> {
        return MappingRemoteAction(remoteAction, PNRemoveMetadataResult::from)
    }
}