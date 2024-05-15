package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult

/**
 * @see [PubNub.deleteChannelGroup]
 */
actual interface DeleteChannelGroup : Endpoint<PNChannelGroupsDeleteGroupResult>