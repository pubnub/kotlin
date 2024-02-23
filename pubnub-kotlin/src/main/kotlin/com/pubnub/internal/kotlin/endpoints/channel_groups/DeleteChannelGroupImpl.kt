package com.pubnub.internal.kotlin.endpoints.channel_groups

import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.channel_groups.IDeleteChannelGroup

/**
 * @see [PubNubImpl.deleteChannelGroup]
 */
class DeleteChannelGroupImpl internal constructor(deleteChannelGroup: IDeleteChannelGroup) :
    IDeleteChannelGroup by deleteChannelGroup,
    DeleteChannelGroup