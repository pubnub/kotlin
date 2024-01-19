package com.pubnub.api.endpoints;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.remoteaction.IdentityMappingEndpoint;
import com.pubnub.api.models.consumer.PNTimeResult;

public class Time extends ValidatingEndpoint<PNTimeResult> {

    public Time(com.pubnub.internal.PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected Endpoint<PNTimeResult> createAction() {
        return new IdentityMappingEndpoint<>(pubnub.time());
    }
}
