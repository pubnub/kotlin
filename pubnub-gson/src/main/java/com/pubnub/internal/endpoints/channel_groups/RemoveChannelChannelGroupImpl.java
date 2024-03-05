package com.pubnub.internal.endpoints.channel_groups;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveChannelChannelGroupImpl extends DelegatingEndpoint<PNChannelGroupsRemoveChannelResult> implements RemoveChannelChannelGroup {
    private String channelGroup;
    private List<String> channels = new ArrayList<>();


    public RemoveChannelChannelGroupImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.EndpointCore<?, PNChannelGroupsRemoveChannelResult> createAction() {
        return pubnub.removeChannelsFromChannelGroup(channels, channelGroup);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channelGroup == null) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_GROUP_MISSING);
        }
    }
}
