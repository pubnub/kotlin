package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult;
import com.pubnub.internal.PubNubImpl;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class ListAllChannelGroup extends Endpoint<PNChannelGroupsListAllResult> {

    public ListAllChannelGroup(PubNubImpl pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNChannelGroupsListAllResult> createAction() {
        return pubnub.listAllChannelGroups();
    }
}
