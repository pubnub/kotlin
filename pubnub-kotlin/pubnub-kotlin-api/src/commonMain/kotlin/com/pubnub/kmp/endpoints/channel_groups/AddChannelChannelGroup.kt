package com.pubnub.kmp.endpoints.channel_groups

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult

/**
 * @see [PubNub.addChannelsToChannelGroup]
 */
interface AddChannelChannelGroup : PNFuture<PNChannelGroupsAddChannelResult>