package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.Endpoint
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.objects.uuid.IRemoveUUIDMetadata
import com.pubnub.internal.endpoints.objects.uuid.RemoveUUIDMetadata

class RemoveUUIDMetadata internal constructor(private val removeUUIDMetadata: RemoveUUIDMetadata) :
    DelegatingEndpoint<PNRemoveMetadataResult>(), IRemoveUUIDMetadata by removeUUIDMetadata {

    override fun createAction(): Endpoint<PNRemoveMetadataResult> = removeUUIDMetadata.map(PNRemoveMetadataResult::from)
}