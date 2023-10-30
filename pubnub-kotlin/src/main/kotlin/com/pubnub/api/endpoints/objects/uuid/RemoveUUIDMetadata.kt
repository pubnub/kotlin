package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.internal.endpoints.objects.uuid.IRemoveUUIDMetadata
import com.pubnub.internal.endpoints.objects.uuid.RemoveUUIDMetadata

class RemoveUUIDMetadata internal constructor(removeUUIDMetadata: RemoveUUIDMetadata) :
    DelegatingEndpoint<PNRemoveMetadataResult, com.pubnub.internal.models.consumer.objects.PNRemoveMetadataResult>(
        removeUUIDMetadata
    ), IRemoveUUIDMetadata by removeUUIDMetadata {
    override fun convertAction(remoteAction: ExtendedRemoteAction<com.pubnub.internal.models.consumer.objects.PNRemoveMetadataResult>): ExtendedRemoteAction<PNRemoveMetadataResult> {
        return MappingRemoteAction(remoteAction, PNRemoveMetadataResult.Companion::from)
    }
}