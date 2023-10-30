package com.pubnub.api.builder;

import com.pubnub.internal.BasePubNub.PubNubImpl;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnsubscribeBuilder extends PubSubBuilder {
    public UnsubscribeBuilder(PubNubImpl pubnub) {
        super(pubnub);
    }

    public void execute() {
        getPubnub().unsubscribe(getChannelSubscriptions(), getChannelGroupSubscriptions());
    }
}
