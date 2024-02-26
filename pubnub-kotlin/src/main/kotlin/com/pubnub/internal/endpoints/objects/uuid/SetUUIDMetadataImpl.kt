package com.pubnub.internal.endpoints.objects.uuid

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.models.consumer.objects.uuid.PNUUIDMetadataResult

/**
 * @see [PubNubImpl.setUUIDMetadata]
 */
class SetUUIDMetadataImpl internal constructor(setUUIDMetadata: SetUUIDMetadata) :
    DelegatingEndpoint<com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult, PNUUIDMetadataResult>(
        setUUIDMetadata,
    ),
    ISetUUIDMetadata by setUUIDMetadata,
    com.pubnub.api.endpoints.objects.uuid.SetUUIDMetadata {
        override fun convertAction(
            remoteAction: ExtendedRemoteAction<PNUUIDMetadataResult>,
        ): ExtendedRemoteAction<com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult> {
            return MappingRemoteAction(remoteAction, com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult::from)
        }
    }
