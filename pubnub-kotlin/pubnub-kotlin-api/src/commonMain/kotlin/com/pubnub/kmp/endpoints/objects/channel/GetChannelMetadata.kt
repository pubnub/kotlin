package com.pubnub.kmp.endpoints.objects.channel

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult

/**
 * @see [PubNub.getChannelMetadata]
 */
interface GetChannelMetadata : PNFuture<PNChannelMetadataResult>