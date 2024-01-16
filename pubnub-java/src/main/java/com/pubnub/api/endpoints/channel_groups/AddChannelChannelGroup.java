package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class AddChannelChannelGroup extends Endpoint<PNChannelGroupsAddChannelResult> {
    @Setter
    private String channelGroup;
    @Setter
    private List<String> channels;

    public AddChannelChannelGroup(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNChannelGroupsAddChannelResult> createAction() {
        return pubnub.addChannelsToChannelGroup(channels, channelGroup);
    }
}
