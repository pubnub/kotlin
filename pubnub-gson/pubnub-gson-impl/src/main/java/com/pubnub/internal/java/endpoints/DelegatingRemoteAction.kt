package com.pubnub.internal.java.endpoints

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.callbacks.Result
import org.jetbrains.annotations.TestOnly
import java.util.function.Consumer

abstract class DelegatingRemoteAction<U, T>(
    @JvmField protected val pubnub: PubNub,
) : ExtendedRemoteAction<T> {
    @get:TestOnly
    internal open val remoteAction: ExtendedRemoteAction<T> by lazy {
        mapResult(createRemoteAction())
    }

    protected abstract fun createRemoteAction(): ExtendedRemoteAction<U>

    protected abstract fun mapResult(action: ExtendedRemoteAction<U>): ExtendedRemoteAction<T>

    @Throws(PubNubException::class)
    override fun sync(): T {
        validateParams()
        return remoteAction.sync()
    }

    @Throws(PubNubException::class)
    protected open fun validateParams() {
    }

    override fun async(callback: Consumer<Result<T>>) {
        try {
            validateParams()
        } catch (pubnubException: PubNubException) {
            callback.accept(Result.failure(pubnubException))
            return
        }
        remoteAction.async(callback)
    }

    override fun retry() {
        remoteAction.retry()
    }

    override fun silentCancel() {
        remoteAction.silentCancel()
    }

    override fun operationType(): PNOperationType {
        return remoteAction.operationType()
    }

    val operationType: PNOperationType
        get() = operationType()
}

abstract class PassthroughRemoteAction<T>(pubnub: PubNub) : DelegatingRemoteAction<T, T>(pubnub) {
    final override fun mapResult(action: ExtendedRemoteAction<T>): ExtendedRemoteAction<T> {
        return action
    }
}
