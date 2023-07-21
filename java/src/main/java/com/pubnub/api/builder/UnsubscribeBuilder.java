package com.pubnub.api.builder;

import com.pubnub.api.builder.dto.UnsubscribeOperation;
import com.pubnub.api.managers.SubscriptionManager;
import com.pubnub.api.subscribe.Subscribe;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnsubscribeBuilder extends PubSubBuilder {

    private final Subscribe subscribe;
    private final boolean enableSubscribeBeta;

    public UnsubscribeBuilder(SubscriptionManager subscriptionManager, Subscribe subscribe, boolean enableSubscribeBeta) {
        super(subscriptionManager);
        this.subscribe = subscribe;
        this.enableSubscribeBeta = enableSubscribeBeta;
    }

    public void execute() {
        if (enableSubscribeBeta) {
            subscribe.unsubscribe(this.getChannelSubscriptions(), this.getChannelGroupSubscriptions());
        } else {
            UnsubscribeOperation unsubscribeOperation = UnsubscribeOperation.builder()
                    .channels(this.getChannelSubscriptions())
                    .channelGroups(this.getChannelGroupSubscriptions())
                    .build();

            this.getSubscriptionManager().adaptUnsubscribeBuilder(unsubscribeOperation);
        }
    }

}
