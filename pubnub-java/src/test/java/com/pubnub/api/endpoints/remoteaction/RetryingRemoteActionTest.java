package com.pubnub.api.endpoints.remoteaction;


import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PNOperationType;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RetryingRemoteActionTest {

    Integer expectedValue = 5;
    int numberOfRetries = 2;
    int timeoutMs = 1000;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Test
    public void whenSucceedsWrappedActionIsCalledOnce() throws PubNubException {
        //given
        TestRemoteAction<Integer> remoteAction = spy(TestRemoteAction.successful(expectedValue));
        RetryingRemoteAction<Integer> retryingRemoteAction = RetryingRemoteAction.autoRetry(remoteAction,
                numberOfRetries,
                PNOperationType.PNFileAction,
                executorService);

        //when
        Integer result = retryingRemoteAction.sync();

        //then
        Assert.assertEquals(expectedValue, result);
        verify(remoteAction, times(1)).sync();
    }

    @Test
    public void whenFailingOnceWrappedActionIsCalledTwice() throws PubNubException {
        //given
        TestRemoteAction<Integer> remoteAction = spy(TestRemoteAction.failingFirstCall(expectedValue));
        RetryingRemoteAction<Integer> retryingRemoteAction = RetryingRemoteAction.autoRetry(remoteAction,
                numberOfRetries,
                PNOperationType.PNFileAction,
                executorService);

        //when
        Integer result = retryingRemoteAction.sync();

        //then
        Assert.assertEquals(expectedValue, result);
        verify(remoteAction, times(numberOfRetries)).sync();
    }

    @Test
    public void whenFailingAlwaysWrappedActionIsCalledTwiceAndThrows() throws PubNubException {
        //given
        TestRemoteAction<Integer> remoteAction = spy(TestRemoteAction.failing());
        RetryingRemoteAction<Integer> retryingRemoteAction = RetryingRemoteAction.autoRetry(remoteAction,
                numberOfRetries,
                PNOperationType.PNFileAction,
                executorService);

        //when
        try {
            retryingRemoteAction.sync();
            fail("Exception expected");
        } catch (PubNubException ex) {
            //then
            verify(remoteAction, times(numberOfRetries)).sync();
        }
    }

    @Test
    public void whenSucceedsWrappedActionIsCalledOnceAndPassesResult() throws InterruptedException {
        //given
        TestRemoteAction<Integer> remoteAction = spy(TestRemoteAction.successful(expectedValue));
        RetryingRemoteAction<Integer> retryingRemoteAction = RetryingRemoteAction.autoRetry(remoteAction,
                numberOfRetries,
                PNOperationType.PNFileAction,
                executorService);
        CountDownLatch asyncSynchronization = new CountDownLatch(1);

        //when
        retryingRemoteAction.async((result, status) -> {
            //then
            Assert.assertEquals(expectedValue, result);
            verify(remoteAction, times(1)).async(any());
            asyncSynchronization.countDown();
        });

        if (!asyncSynchronization.await(3, TimeUnit.SECONDS)) {
            fail("Callback have not been called");
        }

    }

    @Test
    public void whenFailingOnceWrappedActionIsCalledTwiceAndPassesResult() throws InterruptedException {
        //given
        TestRemoteAction<Integer> remoteAction = spy(TestRemoteAction.failingFirstCall(expectedValue));
        RetryingRemoteAction<Integer> retryingRemoteAction = RetryingRemoteAction.autoRetry(remoteAction,
                numberOfRetries,
                PNOperationType.PNFileAction,
                executorService);
        CountDownLatch asyncSynchronization = new CountDownLatch(1);

        //when
        retryingRemoteAction.async((result, status) -> {
            //then
            Assert.assertEquals(expectedValue, result);
            verify(remoteAction, times(numberOfRetries)).async(any());
            asyncSynchronization.countDown();
        });

        if (!asyncSynchronization.await(3, TimeUnit.SECONDS)) {
            fail("Callback have not been called");
        }

    }

    @Test
    public void whenFailingAlwaysWrappedActionIsCalledTwiceAndPassesError() throws InterruptedException {
        //given
        TestRemoteAction<Integer> remoteAction = spy(TestRemoteAction.failing());
        RetryingRemoteAction<Integer> retryingRemoteAction = RetryingRemoteAction.autoRetry(remoteAction,
                numberOfRetries,
                PNOperationType.PNFileAction,
                executorService);
        CountDownLatch asyncSynchronization = new CountDownLatch(1);

        //when
        retryingRemoteAction.async((result, status) -> {
            //then
            Assert.assertTrue(status.isError());
            verify(remoteAction, times(numberOfRetries)).async(any());
            asyncSynchronization.countDown();
        });

        if (!asyncSynchronization.await(3, TimeUnit.SECONDS)) {
            fail("Callback have not been called");
        }

    }

    @Test
    public void whenRetryWrappedActionWillBeCalledTwiceTheUsualTime() throws InterruptedException {
        //given
        TestRemoteAction<Integer> remoteAction = spy(TestRemoteAction.failing());
        RetryingRemoteAction<Integer> retryingRemoteAction = RetryingRemoteAction.autoRetry(remoteAction,
                numberOfRetries,
                PNOperationType.PNFileAction,
                executorService);
        CountDownLatch asyncSynchronization = new CountDownLatch(2);

        //when
        retryingRemoteAction.async((result, status) -> {
            //then
            if (asyncSynchronization.getCount() == 1) {
                Assert.assertTrue(status.isError());
                verify(remoteAction, times(2 * numberOfRetries)).async(any());
            }
            asyncSynchronization.countDown();
            if (asyncSynchronization.getCount() == 1) {
                status.retry();
            }
        });

        if (!asyncSynchronization.await(3, TimeUnit.SECONDS)) {
            fail("Callback have not been called");
        }

    }

}