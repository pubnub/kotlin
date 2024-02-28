package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult;
import com.pubnub.internal.CorePubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class ListAllChannelGroup extends DelegatingEndpoint<PNChannelGroupsListAllResult> {

    public ListAllChannelGroup(CorePubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.CoreEndpoint<?, PNChannelGroupsListAllResult> createAction() {
        return pubnub.listAllChannelGroups();
    }
}
