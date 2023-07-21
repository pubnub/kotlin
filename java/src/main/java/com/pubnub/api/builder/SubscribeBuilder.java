package com.pubnub.api.builder;

import com.pubnub.api.builder.dto.SubscribeOperation;
import com.pubnub.api.managers.SubscriptionManager;
import com.pubnub.api.subscribe.Subscribe;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Accessors(chain = true, fluent = true)
public class SubscribeBuilder extends PubSubBuilder {

    private final Subscribe subscribe;
    private final boolean enableSubscribeBeta;
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

    public SubscribeBuilder(SubscriptionManager subscriptionManager, Subscribe subscribe, boolean enableSubscribeBeta) {
        super(subscriptionManager);
        this.subscribe = subscribe;
        this.enableSubscribeBeta = enableSubscribeBeta;
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
        if (enableSubscribeBeta) {
            subscribe.subscribe(
                    getChannelSubscriptions(), getChannelGroupSubscriptions(), presenceEnabled, timetoken
            );
        } else {
            SubscribeOperation subscribeOperation = SubscribeOperation.builder()
                    .channels(this.getChannelSubscriptions())
                    .channelGroups(this.getChannelGroupSubscriptions())
                    .timetoken(timetoken)
                    .presenceEnabled(presenceEnabled)
                    .build();

            this.getSubscriptionManager().adaptSubscribeBuilder(subscribeOperation);
        }

    }

    public SubscribeBuilder channels(List<String> channels) {
        return (SubscribeBuilder) super.channels(channels);
    }

    public SubscribeBuilder channelGroups(List<String> channelGroups) {
        return (SubscribeBuilder) super.channelGroups(channelGroups);
    }

}
