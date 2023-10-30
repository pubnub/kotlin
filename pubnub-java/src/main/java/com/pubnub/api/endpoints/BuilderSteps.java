package com.pubnub.api.endpoints;

public interface BuilderSteps {
    interface ChannelStep<T> {
        T channel(String channel);
    }
}
