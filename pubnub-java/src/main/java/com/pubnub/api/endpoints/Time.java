package com.pubnub.api.endpoints;

import com.pubnub.api.models.consumer.PNTimeResult;

public class Time extends Endpoint<PNTimeResult> {

    public Time(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNTimeResult> createAction() {
        return pubnub.time();
    }
}
