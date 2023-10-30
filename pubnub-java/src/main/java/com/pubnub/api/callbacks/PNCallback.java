package com.pubnub.api.callbacks;

import com.pubnub.api.PubNubException;
import org.jetbrains.annotations.Nullable;

public interface PNCallback<X> {
    void onResponse(@Nullable X result, @Nullable PubNubException exception);
}
