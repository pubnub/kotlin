package com.pubnub.internal.endpoints.access.builder;

import com.pubnub.api.endpoints.access.GrantToken;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.internal.PubNubCore;
import com.pubnub.internal.endpoints.DelegatingEndpoint;

public abstract class AbstractGrantTokenBuilderImpl<T> extends DelegatingEndpoint<PNGrantTokenResult> {
    protected final GrantToken grantToken;

    public AbstractGrantTokenBuilderImpl(PubNubCore pubnub, GrantToken grantToken) {
        super(pubnub);
        this.grantToken = grantToken;
    }

    @Override
    protected ExtendedRemoteAction<PNGrantTokenResult> createAction() {
        return grantToken;
    }
}
