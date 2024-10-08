package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.deleteChannelGroup]
 */
expect interface DeleteChannelGroup : PNFuture<PNChannelGroupsDeleteGroupResult>
