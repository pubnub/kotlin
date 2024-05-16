package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult

/**
 * @see [PubNub.listAllChannelGroups]
 */
actual interface ListAllChannelGroup : Endpoint<PNChannelGroupsListAllResult>
