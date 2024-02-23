package com.pubnub.internal.kotlin.endpoints.channel_groups

import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.channel_groups.IAllChannelsChannelGroup

/**
 * @see [PubNubImpl.listChannelsForChannelGroup]
 */
class AllChannelsChannelGroupImpl internal constructor(allChannelsChannelGroup: IAllChannelsChannelGroup) :
    IAllChannelsChannelGroup by allChannelsChannelGroup,
    AllChannelsChannelGroup
