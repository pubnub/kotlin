package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.listAllChannelGroups]
 */
expect interface ListAllChannelGroup : PNFuture<PNChannelGroupsListAllResult>
