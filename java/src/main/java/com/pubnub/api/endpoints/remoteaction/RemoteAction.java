package com.pubnub.api.endpoints.remoteaction;

import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.core.CoreRemoteAction;
import org.jetbrains.annotations.NotNull;

public interface RemoteAction<Output> extends CoreRemoteAction<Output, PNStatus> {
    Output sync() throws PubNubException;

    void retry();
}
