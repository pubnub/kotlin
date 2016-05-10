package com.pubnub.api.builder;


import com.pubnub.api.managers.SubscriptionManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public abstract class PubSubBuilder {

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private List<String> channelSubscriptions;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private List<String> channelGroupSubscriptions;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private SubscriptionManager subscriptionManager;

    public PubSubBuilder(SubscriptionManager subscriptionManagerInstance) {
        this.subscriptionManager = subscriptionManagerInstance;
        this.channelSubscriptions = new ArrayList<>();
        this.channelGroupSubscriptions = new ArrayList<>();
    }


    public PubSubBuilder channels(List<String> channel) {
        channelSubscriptions.addAll(channel);
        return this;
    }

    public PubSubBuilder channelGroups(List<String> channelGroup) {
        channelGroupSubscriptions.addAll(channelGroup);
        return this;
    }

    public abstract void execute();

}
