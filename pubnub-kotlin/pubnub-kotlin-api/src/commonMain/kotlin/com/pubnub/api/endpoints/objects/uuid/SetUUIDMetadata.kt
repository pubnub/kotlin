package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult

/**
 * @see [PubNub.setUUIDMetadata]
 */
expect interface SetUUIDMetadata : PNFuture<PNUUIDMetadataResult>