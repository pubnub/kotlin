package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult

/**
 * @see [PubNub.listChannelsForChannelGroup]
 */
interface AllChannelsChannelGroup : Endpoint<PNChannelGroupsAllChannelsResult> {
    val channelGroup: String
}
