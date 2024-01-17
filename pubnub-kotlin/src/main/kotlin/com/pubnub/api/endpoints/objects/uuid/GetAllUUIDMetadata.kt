package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.internal.endpoints.objects.uuid.GetAllUUIDMetadata
import com.pubnub.internal.endpoints.objects.uuid.IGetAllUUIDMetadata
import com.pubnub.internal.models.consumer.objects.uuid.PNUUIDMetadataArrayResult

/**
 * @see [PubNub.getAllUUIDMetadata]
 */
class GetAllUUIDMetadata internal constructor(getAllUUIDMetadata: GetAllUUIDMetadata) :
    DelegatingEndpoint<com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult, PNUUIDMetadataArrayResult>(
        getAllUUIDMetadata
    ), IGetAllUUIDMetadata by getAllUUIDMetadata {
    override fun convertAction(remoteAction: ExtendedRemoteAction<PNUUIDMetadataArrayResult>): ExtendedRemoteAction<com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult> {
        return MappingRemoteAction(
            remoteAction,
            com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult.Companion::from
        )
    }
}