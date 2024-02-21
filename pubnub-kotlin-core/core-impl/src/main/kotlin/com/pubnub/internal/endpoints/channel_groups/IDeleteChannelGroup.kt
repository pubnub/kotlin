package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult

interface IDeleteChannelGroup : ExtendedRemoteAction<PNChannelGroupsDeleteGroupResult> {
    val channelGroup: String
}
