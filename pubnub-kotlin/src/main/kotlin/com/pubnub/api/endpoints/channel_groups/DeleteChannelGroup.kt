package com.pubnub.api.endpoints.channel_groups

import com.pubnub.api.Endpoint
import com.pubnub.internal.PubNubImpl
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult
import com.pubnub.internal.endpoints.channel_groups.IDeleteChannelGroup

/**
 * @see [PubNubImpl.deleteChannelGroup]
 */
class DeleteChannelGroup internal constructor(deleteChannelGroup: IDeleteChannelGroup) :
    Endpoint<PNChannelGroupsDeleteGroupResult>(), IDeleteChannelGroup by deleteChannelGroup
