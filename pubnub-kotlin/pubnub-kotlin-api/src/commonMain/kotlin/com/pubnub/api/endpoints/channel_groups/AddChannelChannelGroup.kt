package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.addChannelsToChannelGroup]
 */
expect interface AddChannelChannelGroup : PNFuture<PNChannelGroupsAddChannelResult>
