package com.pubnub.api.endpoints.channel_groups

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult

class ListAllChannelGroupImpl(pubnub: PubNub) : ListAllChannelGroup,
    EndpointImpl<PubNub.ListAllGroupsResponse, PNChannelGroupsListAllResult>(
        promiseFactory = { pubnub.channelGroups.listGroups() },
        responseMapping = {
            PNChannelGroupsListAllResult(
                it.groups.toList()
            )
        }
    )
