package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult

/**
 * @see [PubNub.getAllChannelMetadata]
 */
expect interface GetAllChannelMetadata :
    Endpoint<PNChannelMetadataArrayResult>