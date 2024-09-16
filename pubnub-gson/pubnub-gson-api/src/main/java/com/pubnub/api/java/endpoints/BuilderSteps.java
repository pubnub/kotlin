package com.pubnub.api.java.endpoints;

public interface BuilderSteps {
    interface ChannelStep<T> {
        T channel(String channel);
    }
}
