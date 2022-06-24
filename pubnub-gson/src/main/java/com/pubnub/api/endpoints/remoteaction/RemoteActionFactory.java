package com.pubnub.api.endpoints.remoteaction;

import com.pubnub.api.PubNubException;

public interface RemoteActionFactory<T, U> {
    RemoteAction<U> create(T input) throws PubNubException;
}
