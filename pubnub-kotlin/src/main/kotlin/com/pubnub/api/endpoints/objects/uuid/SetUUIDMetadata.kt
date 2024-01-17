package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.objects.uuid.ISetUUIDMetadata
import com.pubnub.internal.endpoints.objects.uuid.SetUUIDMetadata

/**
 * @see [PubNub.setUUIDMetadata]
 */
class SetUUIDMetadata internal constructor(private val setUUIDMetadata: SetUUIDMetadata) :
    DelegatingEndpoint<PNUUIDMetadataResult>(), ISetUUIDMetadata by setUUIDMetadata {

    override fun createAction(): Endpoint<PNUUIDMetadataResult> = setUUIDMetadata.map(PNUUIDMetadataResult::from)
}