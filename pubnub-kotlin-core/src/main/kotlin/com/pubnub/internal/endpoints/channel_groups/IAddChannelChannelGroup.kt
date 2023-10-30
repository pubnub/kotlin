package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult

interface IAddChannelChannelGroup : ExtendedRemoteAction<PNChannelGroupsAddChannelResult>{
    val channelGroup: String
    val channels: List<String>
}