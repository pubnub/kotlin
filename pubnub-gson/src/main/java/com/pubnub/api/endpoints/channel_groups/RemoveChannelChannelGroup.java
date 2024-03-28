package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult;

public interface RemoveChannelChannelGroup extends Endpoint<PNChannelGroupsRemoveChannelResult> {
    RemoveChannelChannelGroup channelGroup(String channelGroup);

    RemoveChannelChannelGroup channels(java.util.List<String> channels);
}
