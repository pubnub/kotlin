package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.channel_groups.AllChannelsChannelGroup
import com.pubnub.internal.endpoints.channel_groups.IAllChannelsChannelGroup

/**
 * @see [PubNub.listChannelsForChannelGroup]
 */
class AllChannelsChannelGroup internal constructor(private val allChannelsChannelGroup: AllChannelsChannelGroup) :
    DelegatingEndpoint<PNChannelGroupsAllChannelsResult>(), IAllChannelsChannelGroup by allChannelsChannelGroup {
    override fun createAction(): Endpoint<PNChannelGroupsAllChannelsResult> = allChannelsChannelGroup.mapIdentity()
}
