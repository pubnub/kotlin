package com.pubnub.api.endpoints.presence;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class Heartbeat extends ValidatingEndpoint<Boolean> {

    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;
    @Setter
    private Object state;

    public Heartbeat(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    protected Endpoint<Boolean> createAction() {
        return new IdentityMappingEndpoint<>(new com.pubnub.internal.endpoints.presence.Heartbeat(pubnub, channels, channelGroups, state));
    }
}
