package com.pubnub.internal.endpoints.presence;

import com.pubnub.internal.InternalPubNubClient;
import com.pubnub.internal.endpoints.DelegatingEndpoint;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class HeartbeatImpl extends DelegatingEndpoint<Boolean> implements com.pubnub.api.endpoints.presence.Heartbeat {

    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;
    @Setter
    private Object state;

    public HeartbeatImpl(InternalPubNubClient pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, Boolean> createAction() {
        return new Heartbeat(pubnub, channels, channelGroups, state);
    }
}
