package com.pubnub.internal.endpoints.channel_groups;

import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.IdentityMappingEndpoint;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Accessors(chain = true, fluent = true)
public class ListAllChannelGroupImpl extends IdentityMappingEndpoint<PNChannelGroupsListAllResult> implements ListAllChannelGroup {

    public ListAllChannelGroupImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected EndpointInterface<PNChannelGroupsListAllResult> createAction() {
        return pubnub.listAllChannelGroups();
    }
}
