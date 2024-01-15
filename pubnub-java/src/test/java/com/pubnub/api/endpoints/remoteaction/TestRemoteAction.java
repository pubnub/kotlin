package com.pubnub.api.endpoints.remoteaction;

import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestRemoteAction<Output> implements RemoteAction<Output> {

    private final Output output;
    private final FailingStrategy failingStrategy;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final AtomicInteger asyncCallmeter = new AtomicInteger(0);
    private final AtomicInteger callsToFail;
    private PNCallback<Output> callback;

    TestRemoteAction(Output output, FailingStrategy failingStrategy) {
        this.output = output;
        this.failingStrategy = failingStrategy;
        this.callsToFail = new AtomicInteger(failingStrategy.numberOfCalls);
    }

    public static <T> TestRemoteAction<T> failing() {
        return new TestRemoteAction<>(null, FailingStrategy.ALWAYS_FAIL);
    }

    public static <T> TestRemoteAction<T> failingFirstCall(T output) {
        return new TestRemoteAction<>(output, FailingStrategy.FAIL_FIRST_CALLS);
    }

    public static <T> TestRemoteAction<T> successful(T output) {
        return new TestRemoteAction<>(output, FailingStrategy.NEVER_FAIL);
    }

    @Override
    public Output sync() throws PubNubException {
        if (failingStrategy == FailingStrategy.ALWAYS_FAIL) {
            throw PubNubException.builder().pubnubError(PubNubError.builder().errorCode(500).build()).build();
        } else if (failingStrategy == FailingStrategy.FAIL_FIRST_CALLS && this.callsToFail.getAndDecrement() > 0) {
            throw PubNubException.builder().pubnubError(PubNubError.builder().errorCode(500).build()).build();
        } else {
            return output;
        }
    }

    @Override
    public void async(@NotNull PNCallback<Output> callback) {
        this.callback = callback;
        asyncCallmeter.incrementAndGet();
        executor.execute(() -> {
            if (failingStrategy == FailingStrategy.ALWAYS_FAIL) {
                callback.onResponse(null, PNStatus.builder().error(true).build());
            } else if (failingStrategy == FailingStrategy.FAIL_FIRST_CALLS && this.callsToFail.getAndDecrement() > 0) {
                callback.onResponse(null, PNStatus.builder().error(true).build());
            } else {
                callback.onResponse(output, PNStatus.builder().build());
            }
        });
    }

    @Override
    public void retry() {
        if (callback != null) {
            async(this.callback);
        }
    }

    @Override
    public void silentCancel() {

    }

    public int howManyTimesAsyncCalled() {
        return asyncCallmeter.get();
    }

    enum FailingStrategy {
        NEVER_FAIL(0),
        ALWAYS_FAIL(0),
        FAIL_FIRST_CALLS(1);

        int numberOfCalls;

        FailingStrategy(int numberOfCalls) {
            this.numberOfCalls = numberOfCalls;
        }
    }
}
