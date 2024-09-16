package com.pubnub.internal.java.endpoints.channel_groups;

import com.pubnub.api.Endpoint;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.java.builder.PubNubErrorBuilder;
import com.pubnub.api.java.endpoints.channel_groups.RemoveChannelChannelGroup;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult;
import com.pubnub.internal.java.endpoints.PassthroughEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveChannelChannelGroupImpl extends PassthroughEndpoint<PNChannelGroupsRemoveChannelResult> implements RemoveChannelChannelGroup {
    private String channelGroup;
    @NotNull
    private List<String> channels = new ArrayList<>();

    public RemoveChannelChannelGroupImpl(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected Endpoint<PNChannelGroupsRemoveChannelResult> createRemoteAction() {
        return pubnub.removeChannelsFromChannelGroup(channels, channelGroup);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channelGroup == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_GROUP_MISSING);
        }
    }
}
