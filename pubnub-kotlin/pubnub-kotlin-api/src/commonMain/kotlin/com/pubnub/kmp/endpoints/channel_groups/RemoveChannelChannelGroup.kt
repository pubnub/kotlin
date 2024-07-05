package com.pubnub.kmp.endpoints.channel_groups

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult

/**
 * @see [PubNub.removeChannelsFromChannelGroup]
 */
interface RemoveChannelChannelGroup : PNFuture<PNChannelGroupsRemoveChannelResult> {
}