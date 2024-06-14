package com.pubnub.api.endpoints.channel_groups

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult

/**
 * @see [PubNub.listAllChannelGroups]
 */
actual interface ListAllChannelGroup : PNFuture<PNChannelGroupsListAllResult>

