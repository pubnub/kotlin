package com.pubnub.internal.endpoints;

import com.pubnub.api.endpoints.Time;
import com.pubnub.api.models.consumer.PNTimeResult;
import com.pubnub.internal.CorePubNubClient;

public class TimeImpl extends DelegatingEndpoint<PNTimeResult> implements Time {

    public TimeImpl(CorePubNubClient pubnub) {
        super(pubnub);
    }

    @Override
    protected com.pubnub.internal.CoreEndpoint<?, PNTimeResult> createAction() {
        return pubnub.time();
    }
}
