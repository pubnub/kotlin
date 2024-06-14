package com.pubnub.api.endpoints.objects.channel

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult

/**
 * @see [PubNub.setChannelMetadata]
 */
expect interface SetChannelMetadata : PNFuture<PNChannelMetadataResult>