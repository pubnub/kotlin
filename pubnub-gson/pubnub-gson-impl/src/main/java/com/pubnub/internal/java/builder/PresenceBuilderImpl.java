package com.pubnub.internal.java.builder;

import com.pubnub.api.PubNub;
import com.pubnub.api.java.builder.PresenceBuilder;

import java.util.ArrayList;
import java.util.List;

public class PresenceBuilderImpl implements PresenceBuilder {

    private final PubNub pubnub;
    private boolean connected = false;

    private final List<String> channelSubscriptions = new ArrayList<>();
    private final List<String> channelGroupSubscriptions = new ArrayList<>();

    public PresenceBuilderImpl(PubNub pubNub) {
        this.pubnub = pubNub;
    }

    @Override
    public PresenceBuilder connected(boolean connected) {
        this.connected = connected;
        return this;
    }

    @Override
    public PresenceBuilder channels(List<String> channel) {
        channelSubscriptions.addAll(channel);
        return this;
    }

    @Override
    public PresenceBuilder channelGroups(List<String> channelGroup) {
        channelGroupSubscriptions.addAll(channelGroup);
        return this;
    }

    @Override
    public void execute() {
        pubnub.presence(
                channelSubscriptions,
                channelGroupSubscriptions,
                connected
        );
    }
}
