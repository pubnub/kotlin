package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult;
import com.pubnub.internal.CorePubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class AddChannelChannelGroup extends DelegatingEndpoint<PNChannelGroupsAddChannelResult> {
    @Setter
    private String channelGroup;
    @Setter
    private List<String> channels;

    public AddChannelChannelGroup(CorePubNubClient pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
    }

    @Override
    protected com.pubnub.internal.CoreEndpoint<?, PNChannelGroupsAddChannelResult> createAction() {
        return pubnub.addChannelsToChannelGroup(channels, channelGroup);
    }
}
