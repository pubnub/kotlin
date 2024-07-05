package com.pubnub.kmp.endpoints.channel_groups

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult

class RemoveChannelChannelGroupImpl(pubnub: PubNub, params: PubNub.RemoveChannelParameters) : RemoveChannelChannelGroup,
    EndpointImpl<Any, PNChannelGroupsRemoveChannelResult>(
        promiseFactory = { pubnub.channelGroups.removeChannels(params) },
        responseMapping = {
            PNChannelGroupsRemoveChannelResult()
        }
    )