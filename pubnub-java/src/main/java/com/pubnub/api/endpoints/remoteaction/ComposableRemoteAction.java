package com.pubnub.api.endpoints.remoteaction;

import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import org.jetbrains.annotations.NotNull;

public class ComposableRemoteAction<T, U> implements RemoteAction<U> {
    private final RemoteAction<T> remoteAction;
    private final RemoteActionFactory<T, U> nextRemoteActionFactory;
    private Boolean checkpoint;
    private RemoteAction<U> nextRemoteAction = null;
    private Boolean isCancelled = false;

    public ComposableRemoteAction(RemoteAction<T> remoteAction,
                                  RemoteActionFactory<T, U> nextRemoteActionFactory,
                                  Boolean checkpoint) {
        this.remoteAction = remoteAction;
        this.nextRemoteActionFactory = nextRemoteActionFactory;
        this.checkpoint = checkpoint;
    }

    public static <T> ComposableBuilder<T> firstDo(RemoteAction<T> remoteAction) {
        return new ComposableBuilder<>(remoteAction);
    }

    public <Y> ComposableRemoteAction<U, Y> then(RemoteActionFactory<U, Y> factory) {
        return new ComposableRemoteAction<>(this, factory, false);
    }

    public synchronized ComposableRemoteAction<T, U> checkpoint() {
        checkpoint = true;
        return this;
    }

    @Override
    public U sync() throws PubNubException {
        T result = remoteAction.sync();
        return nextRemoteActionFactory.create(result).sync();
    }

    @Override
    public void async(@NotNull PNCallback<U> callback) {
        remoteAction.async(
                (r, s) -> {
                    if (s.isError()) {
                        callback.onResponse(null, switchRetryReceiver(s));
                    } else {
                        try {
                            synchronized (this) {
                                if (!isCancelled) {
                                    RemoteAction<U> newNextRemoteAction = nextRemoteActionFactory.create(r);
                                    nextRemoteAction = newNextRemoteAction;
                                    newNextRemoteAction.async((r2, s2) -> {
                                                if (s2.isError()) {
                                                    callback.onResponse(null, switchRetryReceiver(s2));
                                                } else {
                                                    callback.onResponse(r2, switchRetryReceiver(s2));
                                                }
                                            }
                                    );
                                }
                            }
                        } catch (PubNubException ex) {
                            callback.onResponse(null, PNStatus.builder()
                                    .category(PNStatusCategory.PNBadRequestCategory)
                                    .error(true)
                                    .build());
                        }

                    }
                }
        );
    }

    private PNStatus switchRetryReceiver(PNStatus s) {
        return s.toBuilder()
                .executedEndpoint(this)
                .build();
    }

    @Override
    public synchronized void retry() {
        if (checkpoint && nextRemoteAction != null) {
            nextRemoteAction.retry();
        } else {
            remoteAction.retry();
        }
    }

    @Override
    public synchronized void silentCancel() {
        isCancelled = true;
        remoteAction.silentCancel();
        if (nextRemoteAction != null) {
            nextRemoteAction.silentCancel();
        }
    }

    public static class ComposableBuilder<T> {

        private final RemoteAction<T> remoteAction;

        private boolean checkpoint;

        public ComposableBuilder(RemoteAction<T> remoteAction) {
            this.remoteAction = remoteAction;
        }

        public <U> ComposableRemoteAction<T, U> then(RemoteActionFactory<T, U> nextRemoteActionFactory) {
            return new ComposableRemoteAction<>(remoteAction, nextRemoteActionFactory, checkpoint);
        }

        public ComposableBuilder<T> checkpoint() {
            this.checkpoint = true;
            return this;
        }

    }
}
