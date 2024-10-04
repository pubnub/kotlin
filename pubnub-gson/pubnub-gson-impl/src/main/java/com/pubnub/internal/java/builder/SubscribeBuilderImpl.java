package com.pubnub.internal.java.builder;

import com.pubnub.api.PubNub;
import com.pubnub.api.java.builder.SubscribeBuilder;

import java.util.ArrayList;
import java.util.List;

public class SubscribeBuilderImpl implements SubscribeBuilder {

    private final PubNub pubnub;
    /**
     * Allow users to specify if they would also like to include the presence channels for those subscriptions.
     */
    private boolean presenceEnabled;

    /**
     * Allow users to subscribe with a custom timetoken.
     */
    private Long timetoken = 0L;

    private List<String> channelSubscriptions = new ArrayList<>();
    private List<String> channelGroupSubscriptions = new ArrayList<>();

    public SubscribeBuilderImpl(PubNub pubnub) {
        this.pubnub = pubnub;
    }

    public SubscribeBuilder channels(List<String> channel) {
        channelSubscriptions.addAll(channel);
        return this;
    }

    public SubscribeBuilder channelGroups(List<String> channelGroup) {
        channelGroupSubscriptions.addAll(channelGroup);
        return this;
    }

    public SubscribeBuilder withPresence() {
        this.presenceEnabled = true;
        return this;
    }

    public SubscribeBuilder withTimetoken(Long timetokenInstance) {
        this.timetoken = timetokenInstance;
        return this;
    }

    public void execute() {
        pubnub.subscribe(
                channelSubscriptions,
                channelGroupSubscriptions,
                /* withPresence = */ presenceEnabled,
                /* withTimetoken = */ timetoken
        );
    }
}
