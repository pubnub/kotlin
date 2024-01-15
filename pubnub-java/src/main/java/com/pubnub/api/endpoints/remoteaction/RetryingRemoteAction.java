package com.pubnub.api.endpoints.remoteaction;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNErrorData;
import com.pubnub.api.models.consumer.PNStatus;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

public class RetryingRemoteAction<T> implements RemoteAction<T> {

    private final RemoteAction<T> remoteAction;
    private final int maxNumberOfAutomaticRetries;
    private final PNOperationType operationType;
    private final ExecutorService executorService;
    private PNCallback<T> cachedCallback;

    public RetryingRemoteAction(RemoteAction<T> remoteAction,
                                int maxNumberOfAutomaticRetries,
                                PNOperationType operationType,
                                ExecutorService executorService) {
        this.remoteAction = remoteAction;
        this.maxNumberOfAutomaticRetries = maxNumberOfAutomaticRetries;
        this.operationType = operationType;
        this.executorService = executorService;
    }

    public static <T> RetryingRemoteAction<T> autoRetry(RemoteAction<T> remoteAction,
                                                        int maxNumberOfAutomaticRetries,
                                                        PNOperationType operationType,
                                                        ExecutorService executorService) {
        return new RetryingRemoteAction<>(remoteAction, maxNumberOfAutomaticRetries, operationType, executorService);
    }

    @Override
    public T sync() throws PubNubException {
        validate();
        PubNubException thrownException = null;
        for (int i = 0; i < maxNumberOfAutomaticRetries; i++) {
            try {
                return remoteAction.sync();
            } catch (PubNubException ex) {
                thrownException = ex;
            }
        }
        //noinspection ConstantConditions
        throw thrownException;
    }

    @Override
    public void async(@NotNull PNCallback<T> callback) {
        cachedCallback = callback;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    validate();
                } catch (PubNubException ex) {
                    callback.onResponse(null,
                            PNStatus.builder()
                                    .executedEndpoint(RetryingRemoteAction.this)
                                    .operation(operationType)
                                    .error(true)
                                    .errorData(new PNErrorData(ex.getErrormsg(), ex))
                                    .build());
                    return;
                }

                ResultAndStatus<T> lastResultAndStatus = null;
                for (int i = 0; i < maxNumberOfAutomaticRetries; i++) {
                    lastResultAndStatus = syncAsync();
                    if (!lastResultAndStatus.status.isError()) {
                        callback.onResponse(lastResultAndStatus.result, lastResultAndStatus.status);
                        return;
                    }
                }
                //noinspection ConstantConditions
                callback.onResponse(lastResultAndStatus.result, lastResultAndStatus.status);
            }
        });
    }

    @Override
    public void retry() {
        async(cachedCallback);
    }

    @Override
    public void silentCancel() {
        remoteAction.silentCancel();
    }

    @Data
    private static class ResultAndStatus<T> {
        private final T result;
        private final PNStatus status;
    }


    private ResultAndStatus<T> syncAsync() {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<ResultAndStatus<T>> atomicResultAndStatus = new AtomicReference<>();

        remoteAction.async(new PNCallback<T>() {
            @Override
            public void onResponse(@Nullable T result, @NotNull PNStatus status) {
                atomicResultAndStatus.set(new ResultAndStatus<>(result,
                        status.toBuilder().executedEndpoint(RetryingRemoteAction.this).build()));
                latch.countDown();
            }
        });

        try {
            latch.await();
            return atomicResultAndStatus.get();
        } catch (InterruptedException e) {
            remoteAction.silentCancel();
            return new ResultAndStatus<>(null,
                    PNStatus.builder()
                            .category(PNStatusCategory.PNUnknownCategory)
                            .operation(operationType)
                            .errorData(new PNErrorData(e.getMessage(), e))
                            .error(true)
                            .executedEndpoint(this)
                            .build());
        }
    }


    private void validate() throws PubNubException {
        if (maxNumberOfAutomaticRetries < 1) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_INVALID_ARGUMENTS)
                    .errormsg("Number of retries cannot be less than 1").build();
        }
    }
}
