package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult;
import com.pubnub.internal.PubNubImpl;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class AllChannelsChannelGroup extends Endpoint<PNChannelGroupsAllChannelsResult> {
    @Setter
    private String channelGroup;

    public AllChannelsChannelGroup(PubNubImpl pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNChannelGroupsAllChannelsResult> createAction() {
        return pubnub.listChannelsForChannelGroup(channelGroup);
    }
}
