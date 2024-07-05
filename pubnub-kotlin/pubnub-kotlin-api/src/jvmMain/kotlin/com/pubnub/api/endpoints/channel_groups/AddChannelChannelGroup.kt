package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult

/**
 * @see [PubNub.addChannelsToChannelGroup]
 */
interface AddChannelChannelGroup : com.pubnub.kmp.endpoints.channel_groups.AddChannelChannelGroup, Endpoint<PNChannelGroupsAddChannelResult> {
    val channelGroup: String
    val channels: List<String>
}
