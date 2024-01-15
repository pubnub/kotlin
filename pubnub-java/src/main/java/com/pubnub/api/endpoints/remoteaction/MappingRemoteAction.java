package com.pubnub.api.endpoints.remoteaction;

import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;
import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;

public class MappingRemoteAction<T, U> implements RemoteAction<U> {
    private final T result;
    private final PNFunction<T, U> function;
    private PNCallback<U> cachedCallback;

    public static <T, U> RemoteAction<U> map(T result, PNFunction<T, U> function) {
        return new MappingRemoteAction<>(result, function);
    }

    private MappingRemoteAction(T result, PNFunction<T, U> function) {
        this.result = result;
        this.function = function;
    }

    @Override
    public U sync() throws PubNubException {
        return function.invoke(result);
    }

    @Override
    public void async(@NotNull PNCallback<U> callback) {
        this.cachedCallback = callback;
        callback.onResponse(function.invoke(result), PNStatus.builder().statusCode(HttpURLConnection.HTTP_OK).build());
    }

    @Override
    public void retry() {
        async(cachedCallback);
    }

    @Override
    public void silentCancel() {

    }
}
