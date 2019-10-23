package com.pubnub.api.callbacks;


import com.pubnub.api.models.consumer.PNStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PNCallback<@Nullable X> {
    public abstract void onResponse(@Nullable X result, @NotNull PNStatus status);
}

