package com.pubnub.core;

public interface CoreRemoteAction<Output, Callback, E extends Throwable> extends Cancelable {
    Output sync() throws E;

    void async(Callback callback);

    void retry();
}
