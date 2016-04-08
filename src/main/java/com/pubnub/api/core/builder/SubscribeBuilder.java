package com.pubnub.api.core.builder;

import com.pubnub.api.managers.SubscriptionManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubscribeBuilder extends PubSubBuilder  {

    @Setter(AccessLevel.NONE)
    private boolean presenceEnabled;

    public SubscribeBuilder(SubscriptionManager subscriptionManager) {
        super(subscriptionManager);
    }

    public SubscribeBuilder withPresence() {
        this.presenceEnabled = true;
        return this;
    }

    public void execute() {
        this.subscriptionManager.adaptSubscribeBuilder(this.channelSubscriptions, this.channelGroupSubscriptions,
                this.presenceEnabled);
    }

    public SubscribeBuilder channel(String channel) {
        return (SubscribeBuilder) super.channel(channel);
    }

    public SubscribeBuilder channels(List<String> channels) {
        return (SubscribeBuilder) super.channels(channels);
    }

    public SubscribeBuilder channelGroup(String channelGroup) {
        return (SubscribeBuilder) super.channelGroup(channelGroup);
    }

    public SubscribeBuilder channelGroups(List<String> channelGroups) {
        return (SubscribeBuilder) super.channelGroups(channelGroups);
    }

}
