package com.pubnub.api.builder;

import com.pubnub.internal.InternalPubNubClient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnsubscribeBuilder extends PubSubBuilder {
    public UnsubscribeBuilder(InternalPubNubClient pubnub) {
        super(pubnub);
    }

    public void execute() {
        getPubnub().unsubscribe(getChannelSubscriptions(), getChannelGroupSubscriptions());
    }
}
