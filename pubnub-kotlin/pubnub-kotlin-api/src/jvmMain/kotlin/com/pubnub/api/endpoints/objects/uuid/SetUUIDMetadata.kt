package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult

/**
 * @see [PubNub.setUUIDMetadata]
 */
interface SetUUIDMetadata : com.pubnub.kmp.endpoints.objects.uuid.SetUUIDMetadata, Endpoint<PNUUIDMetadataResult>
