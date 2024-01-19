package com.pubnub.api.endpoints;

import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.internal.DelegatingEndpoint;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public abstract class ValidatingEndpoint<T> extends DelegatingEndpoint<T> {
    protected final com.pubnub.internal.PubNub pubnub;
    public ValidatingEndpoint(com.pubnub.internal.PubNub pubnub) {
        this.pubnub = pubnub;
    }

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
        getRemoteAction().async(callback);
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
