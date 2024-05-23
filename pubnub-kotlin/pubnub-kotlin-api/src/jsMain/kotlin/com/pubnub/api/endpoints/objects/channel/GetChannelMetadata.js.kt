package com.pubnub.api.endpoints.objects.channel

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult

/**
 * @see [PubNub.getChannelMetadata]
 */
actual interface GetChannelMetadata : Endpoint<PNChannelMetadataResult>