package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.deleteChannelGroup]
 */
class DeleteChannelGroupImpl internal constructor(deleteChannelGroup: IDeleteChannelGroup) :
    IDeleteChannelGroup by deleteChannelGroup,
    DeleteChannelGroup
