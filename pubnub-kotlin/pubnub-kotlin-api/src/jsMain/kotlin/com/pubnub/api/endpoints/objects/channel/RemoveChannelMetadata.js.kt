package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.kmp.PNFuture

actual interface RemoveChannelMetadata : PNFuture<PNRemoveMetadataResult>
