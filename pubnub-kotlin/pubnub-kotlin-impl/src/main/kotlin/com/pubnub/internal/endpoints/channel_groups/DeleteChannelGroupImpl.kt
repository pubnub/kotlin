package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.deleteChannelGroup]
 */
class DeleteChannelGroupImpl internal constructor(deleteChannelGroup: DeleteChannelGroupInterface) :
    DeleteChannelGroupInterface by deleteChannelGroup,
    DeleteChannelGroup,
    EndpointImpl<PNChannelGroupsDeleteGroupResult>(deleteChannelGroup)
