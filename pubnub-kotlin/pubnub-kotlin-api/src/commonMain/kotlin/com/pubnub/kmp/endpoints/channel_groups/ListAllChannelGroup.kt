package com.pubnub.kmp.endpoints.channel_groups

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult

/**
 * @see [PubNub.listAllChannelGroups]
 */
interface ListAllChannelGroup : PNFuture<PNChannelGroupsListAllResult>