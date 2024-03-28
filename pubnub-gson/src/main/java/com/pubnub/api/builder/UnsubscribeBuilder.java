package com.pubnub.api.builder;

import com.pubnub.internal.PubNubCore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnsubscribeBuilder extends PubSubBuilder {
    public UnsubscribeBuilder(PubNubCore pubnub) {
        super(pubnub);
    }

    public void execute() {
        getPubnub().unsubscribe(getChannelSubscriptions(), getChannelGroupSubscriptions());
    }
}
