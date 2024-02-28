package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult;
import com.pubnub.internal.CorePubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class AllChannelsChannelGroup extends DelegatingEndpoint<PNChannelGroupsAllChannelsResult> {
    @Setter
    private String channelGroup;

    public AllChannelsChannelGroup(CorePubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.CoreEndpoint<?, PNChannelGroupsAllChannelsResult> createAction() {
        return pubnub.listChannelsForChannelGroup(channelGroup);
    }
}
