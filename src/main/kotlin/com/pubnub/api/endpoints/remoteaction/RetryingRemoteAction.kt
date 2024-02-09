package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import java.util.concurrent.ExecutorService

internal class RetryingRemoteAction<T>(
    private val remoteAction: ExtendedRemoteAction<T>,
    private val maxNumberOfAutomaticRetries: Int,
    private val executorService: ExecutorService
) : ExtendedRemoteAction<T> {
    private lateinit var cachedCallback: (result: Result<T>) -> Unit

    @Throws(PubNubException::class)
    override fun sync(): T {
        validate()
        var thrownException: PubNubException? = null
        for (i in 0 until maxNumberOfAutomaticRetries) {
            thrownException = try {
                return remoteAction.sync()
            } catch (ex: PubNubException) {
                ex
            }
        }
        throw thrownException!!
    }

    override fun async(callback: (result: Result<T>) -> Unit) {
        cachedCallback = callback
        executorService.execute(
            Runnable {
                try {
                    validate()
                } catch (ex: PubNubException) {
                    callback(
                        Result.failure(ex)
                    )
                    return@Runnable
                }
                var lastException: Throwable? = null
                for (i in 0 until maxNumberOfAutomaticRetries) {
                    try {
                        callback(Result.success(remoteAction.sync()))
                    } catch (e: Throwable) {
                        lastException = e
                    }
                }
                callback(Result.failure(PubNubException.from(lastException!!)))
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
            operationType: PNOperationType,
            executorService: ExecutorService
        ): RetryingRemoteAction<T> {
            return RetryingRemoteAction(remoteAction, maxNumberOfAutomaticRetries, executorService)
        }
    }
}
