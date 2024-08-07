package com.pubnub.api.legacy.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.internal.endpoints.remoteaction.RetryingRemoteAction
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class RetryingRemoteActionTest {
    var expectedValue = 5
    var numberOfRetries = 2
    var timeoutMs = 1000
    var executorService = Executors.newSingleThreadExecutor()

    @Test
    @Throws(PubNubException::class)
    fun whenSucceedsWrappedActionIsCalledOnce() {
        // given
        val remoteAction: TestRemoteAction<Int> = spyk(TestRemoteAction.successful(expectedValue))
        val retryingRemoteAction =
            RetryingRemoteAction.autoRetry(
                remoteAction,
                numberOfRetries,
                executorService,
            )

        // when
        val result = retryingRemoteAction.sync()

        // then
        Assert.assertEquals(expectedValue, result)
        verify(exactly = 1) { remoteAction.sync() }
    }

    @Test
    @Throws(PubNubException::class)
    fun whenFailingOnceWrappedActionIsCalledTwice() {
        // given
        val remoteAction: TestRemoteAction<Int> = spyk(TestRemoteAction.failingFirstCall(expectedValue))
        val retryingRemoteAction =
            RetryingRemoteAction.autoRetry(
                remoteAction,
                numberOfRetries,
                executorService,
            )

        // when
        val result = retryingRemoteAction.sync()

        // then
        Assert.assertEquals(expectedValue, result)
        verify(exactly = numberOfRetries) { remoteAction.sync() }
    }

    @Test
    @Throws(PubNubException::class)
    fun whenFailingAlwaysWrappedActionIsCalledTwiceAndThrows() {
        // given
        val remoteAction: TestRemoteAction<Int?> = spyk(TestRemoteAction.failing())
        val retryingRemoteAction =
            RetryingRemoteAction.autoRetry(
                remoteAction,
                numberOfRetries,
                executorService,
            )

        // when
        try {
            retryingRemoteAction.sync()
            Assert.fail("Exception expected")
        } catch (ex: PubNubException) {
            // then
            verify(exactly = numberOfRetries) { remoteAction.sync() }
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun whenSucceedsWrappedActionIsCalledOnceAndPassesResult() {
        // given
        val remoteAction: TestRemoteAction<Int> = spyk(TestRemoteAction.successful(expectedValue))
        val retryingRemoteAction =
            RetryingRemoteAction.autoRetry(
                remoteAction,
                numberOfRetries,
                executorService,
            )
        val asyncSynchronization = CountDownLatch(1)
        val noFailureCallsSynchronization = CountDownLatch(1)

        // when
        retryingRemoteAction.async { result ->
            // then
            result.onSuccess {
                Assert.assertEquals(expectedValue, it)
                verify(exactly = 1) { remoteAction.sync() }
                asyncSynchronization.countDown()
            }.onFailure {
                noFailureCallsSynchronization.countDown()
            }
        }
        if (!asyncSynchronization.await(3, TimeUnit.SECONDS)) {
            Assert.fail("Callback have not been called")
        }

        if (noFailureCallsSynchronization.await(3, TimeUnit.SECONDS)) {
            Assert.fail("onFailure has been called when it shouldn't!")
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun whenFailingOnceWrappedActionIsCalledTwiceAndPassesResult() {
        // given
        val remoteAction: TestRemoteAction<Int> = spyk(TestRemoteAction.failingFirstCall(expectedValue))
        val retryingRemoteAction =
            RetryingRemoteAction.autoRetry(
                remoteAction,
                numberOfRetries,
                executorService,
            )
        val asyncSynchronization = CountDownLatch(1)

        // when
        retryingRemoteAction.async { result ->
            // then
            result.onSuccess {
                Assert.assertEquals(expectedValue, it)
                verify(exactly = numberOfRetries) { remoteAction.sync() }
                asyncSynchronization.countDown()
            }
        }
        if (!asyncSynchronization.await(3, TimeUnit.SECONDS)) {
            Assert.fail("Callback have not been called")
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun whenFailingAlwaysWrappedActionIsCalledTwiceAndPassesError() {
        // given
        val remoteAction: TestRemoteAction<Int?> = spyk(TestRemoteAction.failing())
        val retryingRemoteAction =
            RetryingRemoteAction.autoRetry(
                remoteAction,
                numberOfRetries,
                executorService,
            )
        val asyncSynchronization = CountDownLatch(1)

        // when
        retryingRemoteAction.async { result ->
            // then
            Assert.assertTrue(result.isFailure)
            verify(exactly = numberOfRetries) { remoteAction.sync() }
            asyncSynchronization.countDown()
        }
        if (!asyncSynchronization.await(3, TimeUnit.SECONDS)) {
            Assert.fail("Callback have not been called")
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun whenRetryWrappedActionWillBeCalledTwiceTheUsualTime() {
        // given
        val remoteAction: TestRemoteAction<Int?> = spyk(TestRemoteAction.failing())
        val retryingRemoteAction =
            RetryingRemoteAction.autoRetry(
                remoteAction,
                numberOfRetries,
                executorService,
            )
        val asyncSynchronization = CountDownLatch(2)

        // when
        retryingRemoteAction.async { result ->
            // then
            if (asyncSynchronization.count == 1L) {
                Assert.assertTrue(result.isFailure)
                verify(exactly = 2 * numberOfRetries) { remoteAction.sync() }
            }
            asyncSynchronization.countDown()
            if (asyncSynchronization.count == 1L) {
                result.onFailure { it.remoteAction?.retry() }
            }
        }
        if (!asyncSynchronization.await(3, TimeUnit.SECONDS)) {
            Assert.fail("Callback have not been called")
        }
    }
}
