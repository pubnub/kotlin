package com.pubnub.api.endpoints.channel_groups

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult

/**
 * @see [PubNub.listAllChannelGroups]
 */
expect interface ListAllChannelGroup : PNFuture<PNChannelGroupsListAllResult>