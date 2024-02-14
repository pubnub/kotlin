package com.pubnub.api.endpoints.access.builder;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.access.GrantToken;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.internal.PubNubImpl;

public abstract class AbstractGrantTokenBuilder<T> extends Endpoint<PNGrantTokenResult> {
    protected final GrantToken grantToken;

    public AbstractGrantTokenBuilder(PubNubImpl pubnub, GrantToken grantToken) {
        super(pubnub);
        this.grantToken = grantToken;
    }

    @Override
    protected ExtendedRemoteAction<PNGrantTokenResult> createAction() {
        return grantToken;
    }
}
