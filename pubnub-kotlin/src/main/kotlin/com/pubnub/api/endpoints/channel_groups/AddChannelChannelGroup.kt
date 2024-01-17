package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.channel_groups.AddChannelChannelGroup
import com.pubnub.internal.endpoints.channel_groups.IAddChannelChannelGroup

/**
 * @see [PubNub.addChannelsToChannelGroup]
 */
class AddChannelChannelGroup internal constructor(private val addChannelChannelGroup: AddChannelChannelGroup) :
    DelegatingEndpoint<PNChannelGroupsAddChannelResult>(), IAddChannelChannelGroup by addChannelChannelGroup {
    override fun createAction(): Endpoint<PNChannelGroupsAddChannelResult> = addChannelChannelGroup.mapIdentity()
}