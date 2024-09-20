package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.setUUIDMetadata]
 */
actual interface SetUUIDMetadata : PNFuture<PNUUIDMetadataResult>
