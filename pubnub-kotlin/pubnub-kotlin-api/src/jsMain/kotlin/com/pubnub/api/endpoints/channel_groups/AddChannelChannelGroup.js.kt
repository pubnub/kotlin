package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.addChannelsToChannelGroup]
 */
actual interface AddChannelChannelGroup : PNFuture<PNChannelGroupsAddChannelResult>
