package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.remoteaction.mapIdentity
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.internal.endpoints.channel_groups.IDeleteChannelGroup

/**
 * @see [PubNub.deleteChannelGroup]
 */
class DeleteChannelGroup internal constructor(private val deleteChannelGroup: DeleteChannelGroup) :
    DelegatingEndpoint<PNChannelGroupsDeleteGroupResult>(), IDeleteChannelGroup by deleteChannelGroup {
    override fun createAction(): Endpoint<PNChannelGroupsDeleteGroupResult> = deleteChannelGroup.mapIdentity()
}
