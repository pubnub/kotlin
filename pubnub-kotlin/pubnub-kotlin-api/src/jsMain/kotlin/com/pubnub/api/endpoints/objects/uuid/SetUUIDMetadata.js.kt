package com.pubnub.api.endpoints.objects.uuid

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult

/**
 * @see [PubNub.setUUIDMetadata]
 */
actual interface SetUUIDMetadata : PNFuture<PNUUIDMetadataResult>

