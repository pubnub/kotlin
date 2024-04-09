package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.internal.EndpointInterface

interface RemoveChannelChannelGroupInterface : EndpointInterface<PNChannelGroupsRemoveChannelResult> {
    val channelGroup: String
    val channels: List<String>
}
