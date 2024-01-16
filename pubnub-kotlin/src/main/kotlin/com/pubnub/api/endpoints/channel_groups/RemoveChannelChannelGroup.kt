package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.internal.endpoints.channel_groups.IRemoveChannelChannelGroup

/**
 * @see [PubNub.removeChannelsFromChannelGroup]
 */
class RemoveChannelChannelGroup internal constructor(removeChannelChannelGroup: IRemoveChannelChannelGroup) :
    Endpoint<PNChannelGroupsRemoveChannelResult>(), IRemoveChannelChannelGroup by removeChannelChannelGroup