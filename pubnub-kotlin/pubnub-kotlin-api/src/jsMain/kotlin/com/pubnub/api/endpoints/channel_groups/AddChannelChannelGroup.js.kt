package com.pubnub.api.endpoints.channel_groups

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult

/**
 * @see [PubNub.addChannelsToChannelGroup]
 */
actual interface AddChannelChannelGroup : PNFuture<PNChannelGroupsAddChannelResult>

