package com.pubnub.api.endpoints.remoteaction;

import com.pubnub.api.callbacks.PNCallback;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;

interface CancellableRemoteAction<T> extends RemoteAction<T> {
    @Override
    default T sync() {
        return null;
    }

    @Override
    default void retry() {

    }

    void doAsync(@NotNull PNCallback<T> callback) throws InterruptedException;

    @Override
    default void async(@NotNull PNCallback<T> callback) {
        //noinspection Convert2Lambda
        Executors.newSingleThreadExecutor()
                .execute(new Runnable() {
                    @Override
                    @SneakyThrows
                    public void run() {
                        doAsync(callback);
                    }
                });
    }
}
