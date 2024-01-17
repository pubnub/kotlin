package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.objects.uuid.GetUUIDMetadata
import com.pubnub.internal.endpoints.objects.uuid.IGetUUIDMetadata

/**
 * @see [PubNub.getUUIDMetadata]
 */
class GetUUIDMetadata internal constructor(private val getUUIDMetadata: GetUUIDMetadata) :
    DelegatingEndpoint<PNUUIDMetadataResult>(),
    IGetUUIDMetadata by getUUIDMetadata {
    override fun createAction(): Endpoint<PNUUIDMetadataResult> = getUUIDMetadata.map(PNUUIDMetadataResult::from)
}
