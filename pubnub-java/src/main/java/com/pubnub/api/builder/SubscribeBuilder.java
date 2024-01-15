package com.pubnub.api.builder;

import com.pubnub.api.builder.dto.SubscribeOperation;
import com.pubnub.api.managers.SubscriptionManager;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class SubscribeBuilder extends PubSubBuilder {

    /**
     * Allow users to specify if they would also like to include the presence channels for those subscriptions.
     */
    @Setter(AccessLevel.NONE)
    private boolean presenceEnabled;

    /**
     * Allow users to subscribe with a custom timetoken.
     */
    @Setter(AccessLevel.NONE)
    private Long timetoken;

    public SubscribeBuilder(SubscriptionManager subscriptionManager) {
        super(subscriptionManager);
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
        SubscribeOperation subscribeOperation = SubscribeOperation.builder()
                .channels(this.getChannelSubscriptions())
                .channelGroups(this.getChannelGroupSubscriptions())
                .timetoken(timetoken)
                .presenceEnabled(presenceEnabled)
                .build();

        this.getSubscriptionManager().adaptSubscribeBuilder(subscribeOperation);
    }

    public SubscribeBuilder channels(List<String> channels) {
        return (SubscribeBuilder) super.channels(channels);
    }

    public SubscribeBuilder channelGroups(List<String> channelGroups) {
        return (SubscribeBuilder) super.channelGroups(channelGroups);
    }

}
