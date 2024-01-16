package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class RemoveChannelChannelGroup extends Endpoint<PNChannelGroupsRemoveChannelResult> {
    @Setter
    private String channelGroup;
    @Setter
    private List<String> channels;


    public RemoveChannelChannelGroup(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNChannelGroupsRemoveChannelResult> createAction() {
        return pubnub.removeChannelsFromChannelGroup(channels, channelGroup);
    }
}
