package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.addChannelsToChannelGroup]
 */
class AddChannelChannelGroupImpl internal constructor(addChannelChannelGroup: IAddChannelChannelGroup) :
    IAddChannelChannelGroup by addChannelChannelGroup,
    AddChannelChannelGroup
