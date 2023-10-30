package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.internal.endpoints.channel_groups.IAddChannelChannelGroup

/**
 * @see [PubNub.addChannelsToChannelGroup]
 */
class AddChannelChannelGroup internal constructor(addChannelChannelGroup: IAddChannelChannelGroup) :
    Endpoint<PNChannelGroupsAddChannelResult>(), IAddChannelChannelGroup by addChannelChannelGroup