package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class DeleteChannelGroup extends DelegatingEndpoint<PNChannelGroupsDeleteGroupResult> {
    @Setter
    private String channelGroup;

    public DeleteChannelGroup(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.EndpointCore<?, PNChannelGroupsDeleteGroupResult> createAction() {
        return pubnub.deleteChannelGroup(channelGroup);
    }
}
