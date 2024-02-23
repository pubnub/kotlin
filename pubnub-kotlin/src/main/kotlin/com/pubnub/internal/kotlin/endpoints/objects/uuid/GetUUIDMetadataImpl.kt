package com.pubnub.internal.kotlin.endpoints.objects.uuid

import com.pubnub.api.DelegatingEndpoint
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.objects.uuid.GetUUIDMetadata
import com.pubnub.internal.endpoints.objects.uuid.IGetUUIDMetadata
import com.pubnub.internal.models.consumer.objects.uuid.PNUUIDMetadataResult as PNUUIDMetadataResultInternal

/**
 * @see [PubNubImpl.getUUIDMetadata]
 */
class GetUUIDMetadataImpl internal constructor(getUUIDMetadata: GetUUIDMetadata) :
    DelegatingEndpoint<PNUUIDMetadataResult, PNUUIDMetadataResultInternal>(getUUIDMetadata),
    IGetUUIDMetadata by getUUIDMetadata,
    com.pubnub.api.endpoints.objects.uuid.GetUUIDMetadata {
    override fun convertAction(remoteAction: ExtendedRemoteAction<PNUUIDMetadataResultInternal>): ExtendedRemoteAction<PNUUIDMetadataResult> {
        return MappingRemoteAction(remoteAction, PNUUIDMetadataResult.Companion::from)
    }
}
