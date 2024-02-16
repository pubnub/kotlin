package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.callbacks.Result
import java.util.concurrent.ExecutorService
import java.util.function.Consumer

internal class RetryingRemoteAction<T>(
    private val remoteAction: ExtendedRemoteAction<T>,
    private val maxNumberOfAutomaticRetries: Int,
    private val executorService: ExecutorService
) : ExtendedRemoteAction<T> {
    private lateinit var cachedCallback: Consumer<Result<T>>

    @Throws(PubNubException::class)
    override fun sync(): T {
        validate()
        var thrownException: PubNubException? = null
        for (i in 0 until maxNumberOfAutomaticRetries) {
            thrownException = try {
                return remoteAction.sync()
            } catch (ex: Throwable) {
                PubNubException.from(ex)
            }
        }
        throw thrownException!!
    }

    override fun async(callback: Consumer<Result<T>>) {
        cachedCallback = callback
        executorService.execute(
            Runnable {
                try {
                    validate()
                } catch (ex: PubNubException) {
                    callback.accept(
                        Result.failure(ex)
                    )
                    return@Runnable
                }
                var lastException: Throwable? = null
                for (i in 0 until maxNumberOfAutomaticRetries) {
                    try {
                        callback.accept(Result.success(remoteAction.sync()))
                    } catch (e: Throwable) {
                        lastException = e
                    }
                }
                callback.accept(Result.failure(PubNubException.from(lastException!!).copy(remoteAction = this)))
            }
        )
    }

    override fun retry() {
        async(cachedCallback)
    }

    override fun silentCancel() {
        remoteAction.silentCancel()
    }

    override fun operationType(): PNOperationType {
        return remoteAction.operationType()
    }

    @Throws(PubNubException::class)
    private fun validate() {
        if (maxNumberOfAutomaticRetries < 1) {
            throw PubNubException(PubNubError.INVALID_ARGUMENTS)
        }
    }

    companion object {
        fun <T> autoRetry(
            remoteAction: ExtendedRemoteAction<T>,
            maxNumberOfAutomaticRetries: Int,
            executorService: ExecutorService
        ): RetryingRemoteAction<T> {
            return RetryingRemoteAction(remoteAction, maxNumberOfAutomaticRetries, executorService)
        }
    }
}
