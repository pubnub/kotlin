package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class ListAllChannelGroup extends ValidatingEndpoint<PNChannelGroupsListAllResult> {

    public ListAllChannelGroup(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNChannelGroupsListAllResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.listAllChannelGroups());
    }
}
