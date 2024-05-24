package com.pubnub.api.endpoints.objects.channel

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult

/**
 * @see [PubNub.getAllChannelMetadata]
 */
actual interface GetAllChannelMetadata :
    Endpoint<PNChannelMetadataArrayResult>