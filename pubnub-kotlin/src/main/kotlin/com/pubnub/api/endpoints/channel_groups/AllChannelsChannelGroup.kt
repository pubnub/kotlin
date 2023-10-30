package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.internal.endpoints.channel_groups.IAllChannelsChannelGroup

/**
 * @see [PubNub.listChannelsForChannelGroup]
 */
class AllChannelsChannelGroup internal constructor(allChannelsChannelGroup: IAllChannelsChannelGroup) :
    Endpoint<PNChannelGroupsAllChannelsResult>(), IAllChannelsChannelGroup by allChannelsChannelGroup