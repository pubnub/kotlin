package com.pubnub.api.endpoints;

import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.v2.callbacks.Result;
import com.pubnub.internal.PubNubImpl;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class Endpoint<T> implements ExtendedRemoteAction<T> {
    protected final PubNubImpl pubnub;
    private volatile ExtendedRemoteAction<T> remoteAction = null;

    public Endpoint(PubNubImpl pubnub) {
        this.pubnub = pubnub;
    }

    protected abstract ExtendedRemoteAction<T> createAction();

    public T sync() throws PubNubException {
        validateParams();
        return getRemoteAction().sync();
    }

//    public void async(@NotNull final PNCallback<T> callback) {
//        try {
//            validateParams();
//        } catch (PubNubException pubnubException) {
//            callback.onResponse(null, pubnubException);
//            return;
//        }
//        getRemoteAction().async(result -> {
//            if (result.isFailure()) {
//                callback.onResponse(null, PubNubException.Companion.from(result.exceptionOrNull()));
//            } else {
//                callback.onResponse(result.getOrNull(), null);
//            }
//            return Unit.INSTANCE;
//        });
//    }

    protected void validateParams() throws PubNubException { }

    protected ExtendedRemoteAction<T> getRemoteAction() {
        ExtendedRemoteAction<T> localRef = remoteAction;
        if (localRef == null) {
            synchronized (this) {
                localRef = remoteAction;
                if (localRef == null) {
                    remoteAction = localRef = createAction();
                }
            }
        }
        return localRef;
    }

//    @Override
//    public void async(@NotNull Function1<? super Result<? extends T>, Unit> callback) {
//        try {
//            validateParams();
//        } catch (PubNubException pubnubException) {
//            callback.invoke(Result.failure(pubnubException));
//            return;
//        }
//        getRemoteAction().async(callback);
//    }

    @Override
    public void async(@NotNull Consumer<Result<T>> callback) {
        try {
            validateParams();
        } catch (PubNubException pubnubException) {
            callback.accept(Result.failure(pubnubException));
            return;
        }
        getRemoteAction().async(callback);
    }

    @Override
    public void retry() {
        getRemoteAction().retry();
    }

    @Override
    public void silentCancel() {
        getRemoteAction().silentCancel();
    }

    @NotNull
    @Override
    public PNOperationType operationType() {
        return getRemoteAction().operationType();
    }

    public PNOperationType getOperationType() {
        return operationType();
    }
}
