package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult;

public interface AllChannelsChannelGroup extends Endpoint<PNChannelGroupsAllChannelsResult> {
    AllChannelsChannelGroup channelGroup(String channelGroup);
}
