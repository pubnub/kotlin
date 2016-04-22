package com.pubnub.api.callbacks;


import com.pubnub.api.core.models.consumer_facing.PNStatus;

public abstract class PNCallback<X> {
    public abstract void onResponse(X result, PNStatus status);
}

