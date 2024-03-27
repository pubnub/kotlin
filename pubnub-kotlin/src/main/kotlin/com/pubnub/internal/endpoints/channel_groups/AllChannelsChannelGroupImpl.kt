package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.listChannelsForChannelGroup]
 */
class AllChannelsChannelGroupImpl internal constructor(allChannelsChannelGroup: AllChannelsChannelGroupInterface) :
    AllChannelsChannelGroupInterface by allChannelsChannelGroup,
    AllChannelsChannelGroup
