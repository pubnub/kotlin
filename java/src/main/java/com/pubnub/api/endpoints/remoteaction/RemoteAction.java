package com.pubnub.api.endpoints.remoteaction;

import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;

public interface RemoteAction<Output> {
    Output sync() throws PubNubException;

    void async(PNCallback<Output> callback);

    void retry();

    void silentCancel();
}
