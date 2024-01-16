package com.pubnub.api.endpoints;

import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public abstract class Endpoint<T> implements ExtendedRemoteAction<T> {
    protected final com.pubnub.internal.PubNub pubnub;
    private volatile ExtendedRemoteAction<T> remoteAction = null;

    public Endpoint(com.pubnub.internal.PubNub pubnub) {
        this.pubnub = pubnub;
    }

    protected abstract ExtendedRemoteAction<T> createAction();

    public T sync() throws PubNubException {
        validateParams();
        return getRemoteAction().sync();
    }

    public void async(@NotNull final PNCallback<T> callback) {
        try {
            validateParams();
        } catch (PubNubException pubnubException) {
            callback.onResponse(null,
                    createStatusResponse(PNStatusCategory.PNBadRequestCategory, pubnubException));
            return;
        }
        getRemoteAction().async((result, pnStatus) -> {
            callback.onResponse(result, pnStatus);
            return Unit.INSTANCE;
        });
    }

    private PNStatus createStatusResponse(PNStatusCategory category, PubNubException throwable) {
        return new PNStatus(
                category,
                true,
                operationType(),
                throwable,
                0,
                false,
                null,
                null,
                null,
                Collections.emptyList(),
                Collections.emptyList()

        );
    }

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

    @Override
    public void async(@NotNull Function2<? super T, ? super PNStatus, Unit> callback) {
        async((result, status) -> {
            callback.invoke(result, status);
        });
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
