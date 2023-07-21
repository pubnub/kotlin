package com.pubnub.api.callbacks;

import com.pubnub.api.models.consumer.PNStatus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PNCallback<@Nullable X> extends com.pubnub.core.PNCallback<X, PNStatus> {
   void onResponse(@Nullable X result, @NotNull PNStatus status);
}
