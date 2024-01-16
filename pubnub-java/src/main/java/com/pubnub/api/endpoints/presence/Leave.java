package com.pubnub.api.endpoints.presence;

import com.pubnub.api.endpoints.Endpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class Leave extends Endpoint<Boolean> {
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
    protected com.pubnub.internal.Endpoint<?, Boolean> createAction() {
        com.pubnub.internal.endpoints.presence.Leave leave = new com.pubnub.internal.endpoints.presence.Leave(pubnub);
        leave.setChannels(channels);
        leave.setChannelGroups(channelGroups);
        return leave;
    }
}
