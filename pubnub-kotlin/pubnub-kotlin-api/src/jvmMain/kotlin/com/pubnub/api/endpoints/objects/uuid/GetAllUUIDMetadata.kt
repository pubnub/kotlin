package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult

/**
 * @see [PubNub.getAllUUIDMetadata]
 */
interface GetAllUUIDMetadata : com.pubnub.kmp.endpoints.objects.uuid.GetAllUUIDMetadata, Endpoint<PNUUIDMetadataArrayResult>
