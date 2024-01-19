package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(chain = true, fluent = true)
public class DeleteChannelGroup extends ValidatingEndpoint<PNChannelGroupsDeleteGroupResult> {
    private String channelGroup;

    public DeleteChannelGroup(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNChannelGroupsDeleteGroupResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.deleteChannelGroup(channelGroup));
    }
}
