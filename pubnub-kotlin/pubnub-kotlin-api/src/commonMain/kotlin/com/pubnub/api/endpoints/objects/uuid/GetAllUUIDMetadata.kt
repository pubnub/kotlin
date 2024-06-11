package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult

/**
 * @see [PubNub.getAllUUIDMetadata]
 */
expect interface GetAllUUIDMetadata : PNFuture<PNUUIDMetadataArrayResult>