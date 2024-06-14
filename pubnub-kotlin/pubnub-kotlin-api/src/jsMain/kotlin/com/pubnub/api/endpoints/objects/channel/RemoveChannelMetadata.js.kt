package com.pubnub.api.endpoints.objects.channel

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult

actual interface RemoveChannelMetadata : PNFuture<PNRemoveMetadataResult>