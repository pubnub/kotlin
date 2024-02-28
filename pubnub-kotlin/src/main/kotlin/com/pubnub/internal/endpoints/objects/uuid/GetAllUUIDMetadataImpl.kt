package com.pubnub.internal.endpoints.objects.uuid

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.models.consumer.objects.uuid.PNUUIDMetadataArrayResult

/**
 * @see [PubNubImpl.getAllUUIDMetadata]
 */
class GetAllUUIDMetadataImpl internal constructor(getAllUUIDMetadata: GetAllUUIDMetadataEndpoint) :
    DelegatingEndpoint<com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult, PNUUIDMetadataArrayResult>(
        getAllUUIDMetadata,
    ),
    GetAllUUIDMetadataInterface by getAllUUIDMetadata,
    com.pubnub.api.endpoints.objects.uuid.GetAllUUIDMetadata {
        override fun convertAction(
            remoteAction: ExtendedRemoteAction<PNUUIDMetadataArrayResult>,
        ): ExtendedRemoteAction<com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult> {
            return MappingRemoteAction(
                remoteAction,
                com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult.Companion::from,
            )
        }
    }
