package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.getAllUUIDMetadata]
 */
actual interface GetAllUUIDMetadata : PNFuture<PNUUIDMetadataArrayResult>
