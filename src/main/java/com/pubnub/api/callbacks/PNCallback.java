package com.pubnub.api.callbacks;


import com.pubnub.api.core.models.consumer.PNStatus;

public abstract class PNCallback<X> {
    public abstract void onResponse(X result, PNStatus status);
}

