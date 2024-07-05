package com.pubnub.kmp.endpoints.objects.uuid

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult

/**
 * @see [PubNub.setUUIDMetadata]
 */
interface SetUUIDMetadata : PNFuture<PNUUIDMetadataResult>