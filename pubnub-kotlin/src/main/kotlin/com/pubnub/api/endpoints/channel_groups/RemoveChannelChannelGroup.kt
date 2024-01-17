package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.channel_groups.IRemoveChannelChannelGroup
import com.pubnub.internal.endpoints.channel_groups.RemoveChannelChannelGroup

/**
 * @see [PubNub.removeChannelsFromChannelGroup]
 */
class RemoveChannelChannelGroup internal constructor(private val removeChannelChannelGroup: RemoveChannelChannelGroup) :
    DelegatingEndpoint<PNChannelGroupsRemoveChannelResult>(), IRemoveChannelChannelGroup by removeChannelChannelGroup {
    override fun createAction(): Endpoint<PNChannelGroupsRemoveChannelResult> = removeChannelChannelGroup.mapIdentity()
}