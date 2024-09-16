package com.pubnub.internal.java.endpoints.channel_groups;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.channel_groups.AllChannelsChannelGroup;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Setter
@Accessors(chain = true, fluent = true)
public class AllChannelsChannelGroupImpl extends PassthroughEndpoint<PNChannelGroupsAllChannelsResult> implements AllChannelsChannelGroup {
    private String channelGroup;

    public AllChannelsChannelGroupImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<PNChannelGroupsAllChannelsResult> createRemoteAction() {
        return pubnub.listChannelsForChannelGroup(channelGroup);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channelGroup == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_GROUP_MISSING);
        }
    }
}
