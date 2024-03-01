package com.pubnub.internal.endpoints;

import com.pubnub.api.endpoints.Time;
import com.pubnub.api.models.consumer.PNTimeResult;
import com.pubnub.internal.PubNubCore;

public class TimeImpl extends DelegatingEndpoint<PNTimeResult> implements Time {

    public TimeImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.EndpointCore<?, PNTimeResult> createAction() {
        return pubnub.time();
    }
}
