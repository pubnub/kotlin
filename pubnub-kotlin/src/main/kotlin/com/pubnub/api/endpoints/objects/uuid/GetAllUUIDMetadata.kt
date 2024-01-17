package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.objects.uuid.GetAllUUIDMetadata
import com.pubnub.internal.endpoints.objects.uuid.IGetAllUUIDMetadata

/**
 * @see [PubNub.getAllUUIDMetadata]
 */
class GetAllUUIDMetadata internal constructor(private val getAllUUIDMetadata: GetAllUUIDMetadata) :
    DelegatingEndpoint<PNUUIDMetadataArrayResult>(), IGetAllUUIDMetadata by getAllUUIDMetadata {

    override fun createAction(): Endpoint<PNUUIDMetadataArrayResult> = getAllUUIDMetadata.map(PNUUIDMetadataArrayResult::from)
}
