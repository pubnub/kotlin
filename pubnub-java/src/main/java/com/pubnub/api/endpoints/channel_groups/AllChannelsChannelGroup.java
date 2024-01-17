package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class AllChannelsChannelGroup extends ValidatingEndpoint<PNChannelGroupsAllChannelsResult> {
    private String channelGroup;

    public AllChannelsChannelGroup(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNChannelGroupsAllChannelsResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.listChannelsForChannelGroup(channelGroup));
    }
}
