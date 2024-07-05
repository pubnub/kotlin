package com.pubnub.kmp.endpoints.channel_groups

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult

/**
 * @see [PubNub.listChannelsForChannelGroup]
 */
interface AllChannelsChannelGroup : PNFuture<PNChannelGroupsAllChannelsResult>