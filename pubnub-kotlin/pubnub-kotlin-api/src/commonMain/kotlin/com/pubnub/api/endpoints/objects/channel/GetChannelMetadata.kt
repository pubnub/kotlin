package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.getChannelMetadata]
 */
expect interface GetChannelMetadata : PNFuture<PNChannelMetadataResult>
