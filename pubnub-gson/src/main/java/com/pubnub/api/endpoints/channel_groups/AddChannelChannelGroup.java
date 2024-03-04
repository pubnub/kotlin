package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult;

public interface AddChannelChannelGroup extends Endpoint<PNChannelGroupsAddChannelResult> {
    AddChannelChannelGroup channelGroup(String channelGroup);

    AddChannelChannelGroup channels(java.util.List<String> channels);
}
