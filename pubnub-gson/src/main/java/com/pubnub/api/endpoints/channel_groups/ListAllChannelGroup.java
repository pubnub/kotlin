package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class ListAllChannelGroup extends DelegatingEndpoint<PNChannelGroupsListAllResult> {

    public ListAllChannelGroup(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.EndpointCore<?, PNChannelGroupsListAllResult> createAction() {
        return pubnub.listAllChannelGroups();
    }
}
