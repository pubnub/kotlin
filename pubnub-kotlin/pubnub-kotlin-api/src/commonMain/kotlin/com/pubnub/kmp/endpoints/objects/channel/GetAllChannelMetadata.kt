package com.pubnub.kmp.endpoints.objects.channel

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult

/**
 * @see [PubNub.getAllChannelMetadata]
 */
interface GetAllChannelMetadata :
    PNFuture<PNChannelMetadataArrayResult>