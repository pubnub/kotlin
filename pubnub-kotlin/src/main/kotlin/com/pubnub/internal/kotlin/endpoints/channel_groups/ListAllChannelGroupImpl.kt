package com.pubnub.internal.kotlin.endpoints.channel_groups

import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.channel_groups.IListAllChannelGroup

/**
 * @see [PubNubImpl.listAllChannelGroups]
 */
class ListAllChannelGroupImpl internal constructor(listAllChannelGroup: IListAllChannelGroup) :
    IListAllChannelGroup by listAllChannelGroup,
    ListAllChannelGroup
