package com.pubnub.api.endpoints.presence;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.internal.PubNubImpl;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true, fluent = true)
public class Heartbeat extends Endpoint<Boolean> {

    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;
    @Setter
    private Object state;

    public Heartbeat(PubNubImpl pubnub) {
        super(pubnub);
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, Boolean> createAction() {
        return new com.pubnub.internal.endpoints.presence.Heartbeat(pubnub, channels, channelGroups, state);
    }
}
