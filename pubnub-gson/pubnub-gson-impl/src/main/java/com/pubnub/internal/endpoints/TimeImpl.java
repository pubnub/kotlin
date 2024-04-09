package com.pubnub.internal.endpoints;

import com.pubnub.api.endpoints.Time;
import com.pubnub.api.models.consumer.PNTimeResult;
import com.pubnub.internal.EndpointInterface;
import com.pubnub.internal.PubNubCore;
import org.jetbrains.annotations.NotNull;

public class TimeImpl extends IdentityMappingEndpoint<PNTimeResult> implements Time {

    public TimeImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    @NotNull
    protected EndpointInterface<PNTimeResult> createAction() {
        return pubnub.time();
    }
}
