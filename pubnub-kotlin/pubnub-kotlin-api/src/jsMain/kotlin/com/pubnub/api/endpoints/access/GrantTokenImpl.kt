package com.pubnub.api.endpoints.access

import PubNub
import com.pubnub.api.EndpointImpl
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult

class GrantTokenImpl(pubnub: PubNub, params: PubNub.GrantTokenParameters) : GrantToken, EndpointImpl<String, PNGrantTokenResult>(
    promiseFactory = { pubnub.grantToken(params) },
    responseMapping = {
        PNGrantTokenResult(it)
    }
)