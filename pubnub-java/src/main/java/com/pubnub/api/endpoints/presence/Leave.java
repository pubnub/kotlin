package com.pubnub.api.endpoints.presence;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class Leave extends ValidatingEndpoint<Boolean> {
    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;

    public Leave(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    protected Endpoint<Boolean> createAction() {
        com.pubnub.internal.endpoints.presence.Leave leave = new com.pubnub.internal.endpoints.presence.Leave(pubnub);
        leave.setChannels(channels);
        leave.setChannelGroups(channelGroups);
        return new IdentityMappingEndpoint<>(leave);
    }
}
