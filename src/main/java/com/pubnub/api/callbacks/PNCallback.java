package com.pubnub.api.callbacks;


import com.pubnub.api.core.models.consumer_facing.PNErrorStatus;

public abstract class PNCallback<X> {
    public abstract void onResponse(X result, PNErrorStatus status);
}

