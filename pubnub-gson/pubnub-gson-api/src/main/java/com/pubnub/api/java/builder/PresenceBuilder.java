package com.pubnub.api.java.builder;

import java.util.List;

public interface PresenceBuilder extends PubSubBuilder {

    PresenceBuilder connected(boolean connected);

    PresenceBuilder channels(List<String> channels);

    PresenceBuilder channelGroups(List<String> channelGroups);
}
