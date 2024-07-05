package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.kmp.endpoints.objects.channel.GetAllChannelMetadata

/**
 * @see [PubNub.getAllChannelMetadata]
 */
interface GetAllChannelMetadata : GetAllChannelMetadata, Endpoint<PNChannelMetadataArrayResult>
