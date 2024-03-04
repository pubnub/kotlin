package com.pubnub.internal.endpoints.channel_groups;

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
    private List<String> channels;


    public RemoveChannelChannelGroupImpl(PubNubCore pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
    }

    @Override
    protected com.pubnub.internal.EndpointCore<?, PNChannelGroupsRemoveChannelResult> createAction() {
        return pubnub.removeChannelsFromChannelGroup(channels, channelGroup);
    }
}
