package com.pubnub.api.endpoints.objects.uuid

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult

/**
 * @see [PubNub.setUUIDMetadata]
 */
actual interface SetUUIDMetadata : Endpoint<PNUUIDMetadataResult>

