package com.pubnub.api.core.builder;

import com.pubnub.api.managers.SubscriptionManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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

}
