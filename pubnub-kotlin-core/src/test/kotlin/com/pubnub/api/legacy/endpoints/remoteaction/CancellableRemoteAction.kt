package com.pubnub.api.legacy.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.v2.callbacks.Result
import java.util.concurrent.Executors
import java.util.function.Consumer

internal interface CancellableRemoteAction<T> : ExtendedRemoteAction<T> {
    override fun sync(): T {
        throw PubNubException("Cancelled")
    }

    override fun retry() {}

    fun doAsync(callback: (result: Result<T>) -> Unit)

    override fun async(callback: Consumer<Result<T>>) {
        Executors.newSingleThreadExecutor()
            .execute {
                doAsync {
                    callback.accept(it)
                }
            }
    }
}
