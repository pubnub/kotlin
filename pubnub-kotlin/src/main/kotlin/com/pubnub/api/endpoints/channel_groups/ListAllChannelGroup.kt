package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.channel_groups.IListAllChannelGroup
import com.pubnub.internal.endpoints.channel_groups.ListAllChannelGroup

/**
 * @see [PubNub.listAllChannelGroups]
 */
class ListAllChannelGroup internal constructor(private val listAllChannelGroup: ListAllChannelGroup) :
    DelegatingEndpoint<PNChannelGroupsListAllResult>(),
    IListAllChannelGroup by listAllChannelGroup {
    override fun createAction(): Endpoint<PNChannelGroupsListAllResult> = listAllChannelGroup.mapIdentity()
}
