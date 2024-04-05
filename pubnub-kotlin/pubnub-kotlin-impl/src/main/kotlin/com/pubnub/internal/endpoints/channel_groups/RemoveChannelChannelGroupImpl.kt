package com.pubnub.internal.endpoints.channel_groups

import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.internal.EndpointImpl
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.removeChannelsFromChannelGroup]
 */
class RemoveChannelChannelGroupImpl internal constructor(removeChannelChannelGroup: RemoveChannelChannelGroupInterface) :
    RemoveChannelChannelGroupInterface by removeChannelChannelGroup,
    RemoveChannelChannelGroup,
    EndpointImpl<PNChannelGroupsRemoveChannelResult>(removeChannelChannelGroup)
