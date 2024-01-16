package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class DeleteChannelGroup extends Endpoint<PNChannelGroupsDeleteGroupResult> {
    @Setter
    private String channelGroup;

    public DeleteChannelGroup(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNChannelGroupsDeleteGroupResult> createAction() {
        return pubnub.deleteChannelGroup(channelGroup);
    }
}
