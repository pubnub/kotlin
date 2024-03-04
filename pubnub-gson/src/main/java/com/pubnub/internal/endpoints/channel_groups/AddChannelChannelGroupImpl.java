package com.pubnub.internal.endpoints.channel_groups;

import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class AddChannelChannelGroupImpl extends DelegatingEndpoint<PNChannelGroupsAddChannelResult> implements AddChannelChannelGroup {
    private String channelGroup;
    private List<String> channels;

    public AddChannelChannelGroupImpl(PubNubCore pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
    }

    @Override
    protected com.pubnub.internal.EndpointCore<?, PNChannelGroupsAddChannelResult> createAction() {
        return pubnub.addChannelsToChannelGroup(channels, channelGroup);
    }
}
