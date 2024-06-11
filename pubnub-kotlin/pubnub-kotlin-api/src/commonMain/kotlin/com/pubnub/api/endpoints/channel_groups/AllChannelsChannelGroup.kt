package com.pubnub.api.endpoints.channel_groups

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult

/**
 * @see [PubNub.listChannelsForChannelGroup]
 */
expect interface AllChannelsChannelGroup : PNFuture<PNChannelGroupsAllChannelsResult>