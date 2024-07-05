package com.pubnub.kmp.endpoints.objects.uuid

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult

/**
 * @see [PubNub.getAllUUIDMetadata]
 */
interface GetAllUUIDMetadata : PNFuture<PNUUIDMetadataArrayResult>