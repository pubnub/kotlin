package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.getAllChannelMetadata]
 */
actual interface GetAllChannelMetadata :
    PNFuture<PNChannelMetadataArrayResult>
