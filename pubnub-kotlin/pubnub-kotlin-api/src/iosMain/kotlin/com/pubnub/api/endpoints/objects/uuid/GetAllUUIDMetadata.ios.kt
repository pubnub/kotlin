package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult

/**
 * @see [PubNub.getAllUUIDMetadata]
 */
actual interface GetAllUUIDMetadata : Endpoint<PNUUIDMetadataArrayResult>