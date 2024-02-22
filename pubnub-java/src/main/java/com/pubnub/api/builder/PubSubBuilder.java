package com.pubnub.api.builder;


import com.pubnub.internal.InternalPubNubClient;
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
    private InternalPubNubClient pubnub;

    public PubSubBuilder(InternalPubNubClient pubnub) {
        this.pubnub = pubnub;
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
