package com.pubnub.api.endpoints.access.builder;

import com.pubnub.api.Endpoint;
import com.pubnub.api.endpoints.ValidatingEndpoint;
import com.pubnub.api.endpoints.access.GrantToken;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;

public abstract class AbstractGrantTokenBuilder<T> extends ValidatingEndpoint<PNGrantTokenResult> {
    protected final GrantToken grantToken;

    public AbstractGrantTokenBuilder(com.pubnub.internal.PubNub pubnub, GrantToken grantToken) {
        super(pubnub);
        this.grantToken = grantToken;
    }

    @Override
    protected Endpoint<PNGrantTokenResult> createAction() {
        return grantToken;
    }
}
