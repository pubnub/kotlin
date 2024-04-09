package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult
import com.pubnub.internal.EndpointInterface

interface DeleteChannelGroupInterface : EndpointInterface<PNChannelGroupsDeleteGroupResult> {
    val channelGroup: String
}
