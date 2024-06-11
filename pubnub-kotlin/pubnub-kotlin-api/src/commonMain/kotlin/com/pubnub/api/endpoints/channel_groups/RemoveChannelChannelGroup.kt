package com.pubnub.api.endpoints.channel_groups

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult

/**
 * @see [PubNub.removeChannelsFromChannelGroup]
 */
expect interface RemoveChannelChannelGroup : PNFuture<PNChannelGroupsRemoveChannelResult> {
}