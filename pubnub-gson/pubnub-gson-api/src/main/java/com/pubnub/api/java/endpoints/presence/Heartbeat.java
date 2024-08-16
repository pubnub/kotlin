package com.pubnub.api.java.endpoints.presence;

import com.pubnub.api.java.endpoints.Endpoint;

public interface Heartbeat extends Endpoint<Boolean> {
    Heartbeat channels(java.util.List<String> channels);

    Heartbeat channelGroups(java.util.List<String> channelGroups);

    Heartbeat state(Object state);
}
