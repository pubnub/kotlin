package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult

expect interface RemoveUUIDMetadata : PNFuture<PNRemoveMetadataResult> {
}