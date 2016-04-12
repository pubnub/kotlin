package com.pubnub.api.callbacks;


import com.pubnub.api.core.ErrorStatus;

public abstract class PNCallback<X> {
    public abstract void onResponse(X result, ErrorStatus status);
}
