package com.pubnub.api.legacy.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.v2.callbacks.Result
import java.util.concurrent.Executors

internal interface CancellableRemoteAction<T> : ExtendedRemoteAction<T> {
    override fun sync(): T {
        throw PubNubException("Cancelled")
    }

    override fun retry() {}

    fun doAsync(callback: (result: Result<T>) -> Unit)
    override fun async(callback: (Result<T>) -> Unit) {
        Executors.newSingleThreadExecutor()
            .execute { doAsync(callback) }
    }
}
