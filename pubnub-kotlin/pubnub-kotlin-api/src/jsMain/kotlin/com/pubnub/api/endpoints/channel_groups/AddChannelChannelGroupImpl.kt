package com.pubnub.kmp.endpoints.channel_groups

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult

class AddChannelChannelGroupImpl(pubnub: PubNub, params: PubNub.AddChannelParameters) : AddChannelChannelGroup, EndpointImpl<Any, PNChannelGroupsAddChannelResult>(
    promiseFactory = { pubnub.channelGroups.addChannels(params) },
    responseMapping = {
        PNChannelGroupsAddChannelResult()
    }
)