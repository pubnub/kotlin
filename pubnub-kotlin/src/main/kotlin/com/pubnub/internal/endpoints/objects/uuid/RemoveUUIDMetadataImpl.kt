package com.pubnub.internal.endpoints.objects.uuid

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.internal.DelegatingEndpoint

class RemoveUUIDMetadataImpl internal constructor(removeUUIDMetadata: RemoveUUIDMetadataEndpoint) :
    DelegatingEndpoint<PNRemoveMetadataResult, com.pubnub.internal.models.consumer.objects.PNRemoveMetadataResult>(
        removeUUIDMetadata,
    ),
    RemoveUUIDMetadataInterface by removeUUIDMetadata,
    com.pubnub.api.endpoints.objects.uuid.RemoveUUIDMetadata {
        override fun convertAction(
            remoteAction: ExtendedRemoteAction<com.pubnub.internal.models.consumer.objects.PNRemoveMetadataResult>,
        ): ExtendedRemoteAction<PNRemoveMetadataResult> {
            return MappingRemoteAction(remoteAction, PNRemoveMetadataResult.Companion::from)
        }
    }
