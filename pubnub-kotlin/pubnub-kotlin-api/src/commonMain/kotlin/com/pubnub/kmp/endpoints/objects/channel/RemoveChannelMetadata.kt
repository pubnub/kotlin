package com.pubnub.kmp.endpoints.objects.channel

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult

interface RemoveChannelMetadata : PNFuture<PNRemoveMetadataResult>