package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult;

public interface DeleteChannelGroup extends Endpoint<PNChannelGroupsDeleteGroupResult> {
    DeleteChannelGroup channelGroup(String channelGroup);
}
