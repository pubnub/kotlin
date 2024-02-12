package com.pubnub.api.legacy.endpoints.remoteaction

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class TestRemoteAction<Output> internal constructor(
    private val output: Output?,
    private val failingStrategy: FailingStrategy
) : ExtendedRemoteAction<Output> {
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val asyncCallmeter = AtomicInteger(0)
    private val callsToFail: AtomicInteger
    private lateinit var callback: (result: Result<Output>) -> Unit

    @Throws(PubNubException::class)
    override fun sync(): Output {
        return if (failingStrategy == FailingStrategy.ALWAYS_FAIL) {
            throw PubNubException(PubNubError.INTERNAL_ERROR)
        } else if (failingStrategy == FailingStrategy.FAIL_FIRST_CALLS && callsToFail.getAndDecrement() > 0) {
            throw PubNubException(PubNubError.INTERNAL_ERROR)
        } else {
            output!!
        }
    }

    override fun async(callback: (result: Result<Output>) -> Unit) {
        this.callback = callback
        asyncCallmeter.incrementAndGet()
        executor.execute {
            if (failingStrategy == FailingStrategy.ALWAYS_FAIL) {
                callback(Result.failure(PubNubException()))
            } else if (failingStrategy == FailingStrategy.FAIL_FIRST_CALLS && callsToFail.getAndDecrement() > 0) {
                callback(Result.failure(PubNubException()))
            } else {
                callback(Result.success(output!!))
            }
        }
    }

    override fun retry() {
        async(callback)
    }

    override fun silentCancel() {}
    fun howManyTimesAsyncCalled(): Int {
        return asyncCallmeter.get()
    }

    internal enum class FailingStrategy(var numberOfCalls: Int) {
        NEVER_FAIL(0), ALWAYS_FAIL(0), FAIL_FIRST_CALLS(1);
    }

    companion object {
        fun <T> failing(): TestRemoteAction<T> {
            return TestRemoteAction(
                null,
                FailingStrategy.ALWAYS_FAIL
            )
        }

        fun <T> failingFirstCall(output: T): TestRemoteAction<T> {
            return TestRemoteAction(
                output,
                FailingStrategy.FAIL_FIRST_CALLS
            )
        }

        fun <T> successful(
            output: T
        ): TestRemoteAction<T> {
            return TestRemoteAction(
                output,
                FailingStrategy.NEVER_FAIL
            )
        }
    }

    init {
        callsToFail = AtomicInteger(failingStrategy.numberOfCalls)
    }

    override fun operationType(): PNOperationType {
        return PNOperationType.FileOperation
    }
}
