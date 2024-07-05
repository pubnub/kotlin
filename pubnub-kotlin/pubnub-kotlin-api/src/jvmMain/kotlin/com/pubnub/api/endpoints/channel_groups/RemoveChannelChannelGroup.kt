package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult

/**
 * @see [PubNub.removeChannelsFromChannelGroup]
 */
interface RemoveChannelChannelGroup : com.pubnub.kmp.endpoints.channel_groups.RemoveChannelChannelGroup, Endpoint<PNChannelGroupsRemoveChannelResult> {
    val channelGroup: String
    val channels: List<String>
}
