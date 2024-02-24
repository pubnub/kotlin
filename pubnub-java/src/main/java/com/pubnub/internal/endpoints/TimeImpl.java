package com.pubnub.internal.endpoints;

import com.pubnub.api.endpoints.Time;
import com.pubnub.api.models.consumer.PNTimeResult;
import com.pubnub.internal.InternalPubNubClient;

public class TimeImpl extends DelegatingEndpoint<PNTimeResult>implements Time {

    public TimeImpl(InternalPubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.Endpoint<?, PNTimeResult> createAction() {
        return pubnub.time();
    }
}
