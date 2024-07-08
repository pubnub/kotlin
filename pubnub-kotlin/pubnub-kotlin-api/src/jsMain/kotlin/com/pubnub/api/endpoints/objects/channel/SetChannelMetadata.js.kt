package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.setChannelMetadata]
 */
actual interface SetChannelMetadata : PNFuture<PNChannelMetadataResult>
