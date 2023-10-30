package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult
import com.pubnub.internal.endpoints.channel_groups.IListAllChannelGroup

/**
 * @see [PubNub.listAllChannelGroups]
 */
class ListAllChannelGroup internal constructor(listAllChannelGroup: IListAllChannelGroup) :
    Endpoint<PNChannelGroupsListAllResult>(),
    IListAllChannelGroup by listAllChannelGroup