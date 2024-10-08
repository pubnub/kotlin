package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult

/**
 * @see [PubNub.getUUIDMetadata]
 */
actual interface GetUUIDMetadata : Endpoint<PNUUIDMetadataResult> {
    val uuid: String
}
