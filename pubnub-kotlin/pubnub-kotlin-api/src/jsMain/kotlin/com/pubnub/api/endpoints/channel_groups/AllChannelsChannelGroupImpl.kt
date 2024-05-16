package com.pubnub.api.endpoints.channel_groups

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult

class AllChannelsChannelGroupImpl(pubnub: PubNub, params: PubNub.ListChannelsParameters) : AllChannelsChannelGroup, EndpointImpl<PubNub.ListChannelsResponse, PNChannelGroupsAllChannelsResult>(
    promiseFactory = { pubnub.channelGroups.listChannels(params) },
    responseMapping = {
        PNChannelGroupsAllChannelsResult(
            it.channels.toList()
        )
    }
)