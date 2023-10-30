package com.pubnub.api.endpoints;

import com.pubnub.api.models.consumer.PNTimeResult;
import com.pubnub.internal.BasePubNub.PubNubImpl;

public class Time extends Endpoint<PNTimeResult> {

    public Time(PubNubImpl pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNTimeResult> createAction() {
        return pubnub.time();
    }
}
