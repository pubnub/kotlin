package com.pubnub.internal.endpoints.channel_groups;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.IdentityMappingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class AllChannelsChannelGroupImpl extends IdentityMappingEndpoint<PNChannelGroupsAllChannelsResult> implements AllChannelsChannelGroup {
    private String channelGroup;

    public AllChannelsChannelGroupImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected EndpointInterface<PNChannelGroupsAllChannelsResult> createAction() {
        return pubnub.listChannelsForChannelGroup(channelGroup);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channelGroup == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_GROUP_MISSING);
        }
    }
}
