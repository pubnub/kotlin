package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.kmp.PNFuture

expect interface RemoveUUIDMetadata : PNFuture<PNRemoveMetadataResult>
