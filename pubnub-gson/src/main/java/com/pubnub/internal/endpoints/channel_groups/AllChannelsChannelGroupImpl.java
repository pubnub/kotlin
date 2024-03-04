package com.pubnub.internal.endpoints.channel_groups;

import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class AllChannelsChannelGroupImpl extends DelegatingEndpoint<PNChannelGroupsAllChannelsResult> implements AllChannelsChannelGroup {
    @Setter
    private String channelGroup;

    public AllChannelsChannelGroupImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.EndpointCore<?, PNChannelGroupsAllChannelsResult> createAction() {
        return pubnub.listChannelsForChannelGroup(channelGroup);
    }
}
