package com.pubnub.api.java.endpoints.presence;

import com.pubnub.api.java.endpoints.Endpoint;

public interface Leave extends Endpoint<Boolean> {
    Leave channels(java.util.List<String> channels);

    Leave channelGroups(java.util.List<String> channelGroups);
}
