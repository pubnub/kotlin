package com.pubnub.api.core;


public abstract class PnCallback<X> {
    public abstract void status(ErrorStatus status);
    public abstract void result(X result);

}
