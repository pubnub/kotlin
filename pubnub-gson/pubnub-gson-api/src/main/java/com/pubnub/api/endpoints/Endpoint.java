package com.pubnub.api.endpoints;

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.v2.PNConfiguration;

public interface Endpoint<T> extends ExtendedRemoteAction<T> {
    void overrideConfiguration(PNConfiguration configuration);
}
