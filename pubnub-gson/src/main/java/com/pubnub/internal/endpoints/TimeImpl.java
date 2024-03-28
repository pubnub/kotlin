package com.pubnub.internal.endpoints;

import com.pubnub.api.endpoints.Time;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.PNTimeResult;
import com.pubnub.internal.PubNubCore;

public class TimeImpl extends DelegatingEndpoint<PNTimeResult> implements Time {

    public TimeImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @Override
    protected ExtendedRemoteAction<PNTimeResult> createAction() {
        return pubnub.time();
    }
}
