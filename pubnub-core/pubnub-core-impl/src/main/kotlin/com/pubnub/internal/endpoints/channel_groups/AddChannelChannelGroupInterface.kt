package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.internal.EndpointInterface

interface AddChannelChannelGroupInterface : EndpointInterface<PNChannelGroupsAddChannelResult> {
    val channelGroup: String
    val channels: List<String>
}
