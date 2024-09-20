package com.pubnub.api.java.endpoints.channel_groups;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult;

public interface DeleteChannelGroup extends Endpoint<PNChannelGroupsDeleteGroupResult> {
    DeleteChannelGroup channelGroup(String channelGroup);
}
