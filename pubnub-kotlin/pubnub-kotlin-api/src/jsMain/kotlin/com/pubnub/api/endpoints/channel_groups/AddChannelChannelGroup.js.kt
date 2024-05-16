package com.pubnub.api.endpoints.channel_groups

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult

/**
 * @see [PubNub.addChannelsToChannelGroup]
 */
actual interface AddChannelChannelGroup : Endpoint<PNChannelGroupsAddChannelResult>

