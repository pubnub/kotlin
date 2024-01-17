package com.pubnub.api.legacy.endpoints.remoteaction

import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import java.util.concurrent.Executors

internal interface CancellableRemoteAction<T> : ExtendedRemoteAction<T> {
    override fun sync(): T? {
        return null
    }

    override fun retry() {}

    fun doAsync(callback: PNCallback<T>)
    override fun async(callback: PNCallback<T>) {
        Executors.newSingleThreadExecutor()
            .execute { doAsync(callback) }
    }
}
