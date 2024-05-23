package com.pubnub.api.endpoints.channel_groups

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult

class DeleteChannelGroupImpl(pubnub: PubNub, params: PubNub.DeleteGroupParameters) : DeleteChannelGroup, EndpointImpl<Any, PNChannelGroupsDeleteGroupResult>(
    promiseFactory = { pubnub.channelGroups.deleteGroup(params) },
    responseMapping = {
        PNChannelGroupsDeleteGroupResult()
    }
)