package com.pubnub.api.endpoints.channel_groups

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult

/**
 * @see [PubNub.deleteChannelGroup]
 */
actual interface DeleteChannelGroup : PNFuture<PNChannelGroupsDeleteGroupResult>