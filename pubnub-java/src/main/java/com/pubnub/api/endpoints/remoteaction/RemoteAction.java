package com.pubnub.api.endpoints.remoteaction;

import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import org.jetbrains.annotations.NotNull;

public interface RemoteAction<Output> {
    Output sync() throws PubNubException;

    void async(@NotNull PNCallback<Output> callback);

    void retry();

    void silentCancel();
}
