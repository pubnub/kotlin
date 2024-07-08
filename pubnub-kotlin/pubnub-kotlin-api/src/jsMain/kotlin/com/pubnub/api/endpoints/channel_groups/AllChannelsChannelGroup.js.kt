package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.listChannelsForChannelGroup]
 */
actual interface AllChannelsChannelGroup : PNFuture<PNChannelGroupsAllChannelsResult>
