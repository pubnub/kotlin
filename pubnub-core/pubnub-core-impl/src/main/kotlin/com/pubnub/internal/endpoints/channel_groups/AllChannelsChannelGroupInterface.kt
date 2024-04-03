package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.internal.EndpointInterface

interface AllChannelsChannelGroupInterface : EndpointInterface<PNChannelGroupsAllChannelsResult> {
    val channelGroup: String
}
