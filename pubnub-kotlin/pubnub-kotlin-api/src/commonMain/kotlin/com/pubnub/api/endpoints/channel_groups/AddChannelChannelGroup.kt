package com.pubnub.api.endpoints.channel_groups

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult

/**
 * @see [PubNub.addChannelsToChannelGroup]
 */
expect interface AddChannelChannelGroup : PNFuture<PNChannelGroupsAddChannelResult>