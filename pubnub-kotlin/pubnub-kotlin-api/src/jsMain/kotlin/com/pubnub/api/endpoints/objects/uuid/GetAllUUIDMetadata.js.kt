package com.pubnub.api.endpoints.objects.uuid

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult

/**
 * @see [PubNub.getAllUUIDMetadata]
 */
actual interface GetAllUUIDMetadata : PNFuture<PNUUIDMetadataArrayResult>