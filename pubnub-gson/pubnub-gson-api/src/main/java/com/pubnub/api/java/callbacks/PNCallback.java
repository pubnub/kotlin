package com.pubnub.api.java.callbacks;

import com.pubnub.api.PubNubException;
import com.pubnub.api.v2.callbacks.Result;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface PNCallback<X> extends Consumer<Result<X>> {
    void onResponse(@Nullable X result, @Nullable PubNubException exception);

    @Override
    default void accept(Result<X> xResult) {
        onResponse(xResult.getOrNull(), xResult.exceptionOrNull());
    }
}
