package com.pubnub.internal.java.builder;

import com.pubnub.api.PubNub;
import com.pubnub.api.java.builder.UnsubscribeBuilder;

import java.util.ArrayList;
import java.util.List;

public class UnsubscribeBuilderImpl implements UnsubscribeBuilder {

    private final PubNub pubnub;
    private List<String> channelSubscriptions = new ArrayList<>();
    private List<String> channelGroupSubscriptions = new ArrayList<>();

    public UnsubscribeBuilderImpl(PubNub pubnub) {
        this.pubnub = pubnub;
    }

    public UnsubscribeBuilder channels(List<String> channel) {
        channelSubscriptions.addAll(channel);
        return this;
    }

    public UnsubscribeBuilder channelGroups(List<String> channelGroup) {
        channelGroupSubscriptions.addAll(channelGroup);
        return this;
    }

    public void execute() {
        pubnub.unsubscribe(channelSubscriptions, channelGroupSubscriptions);
    }
}
