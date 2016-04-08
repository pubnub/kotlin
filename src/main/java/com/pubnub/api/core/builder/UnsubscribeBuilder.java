package com.pubnub.api.core.builder;

import com.pubnub.api.managers.SubscriptionManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnsubscribeBuilder extends PubSubBuilder  {

    public UnsubscribeBuilder(SubscriptionManager subscriptionManager) {
        super(subscriptionManager);
    }

    public void execute() {
        this.subscriptionManager.adaptUnsubscribeBuilder(this.channelSubscriptions, this.channelGroupSubscriptions);
    }

}
