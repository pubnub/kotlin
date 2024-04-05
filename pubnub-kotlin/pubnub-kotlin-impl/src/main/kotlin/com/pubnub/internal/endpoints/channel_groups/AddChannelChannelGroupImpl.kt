package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.addChannelsToChannelGroup]
 */
class AddChannelChannelGroupImpl internal constructor(addChannelChannelGroup: AddChannelChannelGroupInterface) :
    AddChannelChannelGroupInterface by addChannelChannelGroup,
    AddChannelChannelGroup,
    EndpointImpl<PNChannelGroupsAddChannelResult>(addChannelChannelGroup)
