package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class RemoveChannelChannelGroup extends ValidatingEndpoint<PNChannelGroupsRemoveChannelResult> {
    private String channelGroup;
    private List<String> channels;


    public RemoveChannelChannelGroup(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
    }

    @Override
    protected Endpoint<PNChannelGroupsRemoveChannelResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.removeChannelsFromChannelGroup(channels, channelGroup));
    }
}
