package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.removeChannelsFromChannelGroup]
 */
class RemoveChannelChannelGroupImpl internal constructor(removeChannelChannelGroup: IRemoveChannelChannelGroup) :
    IRemoveChannelChannelGroup by removeChannelChannelGroup,
    RemoveChannelChannelGroup
