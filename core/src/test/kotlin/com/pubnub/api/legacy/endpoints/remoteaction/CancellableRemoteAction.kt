package com.pubnub.api.legacy.endpoints.remoteaction

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.PNStatus
import java.util.concurrent.Executors

internal interface CancellableRemoteAction<T> : ExtendedRemoteAction<T> {
    override fun sync(): T? {
        return null
    }

    override fun retry() {}

    fun doAsync(callback: (result: T?, status: PNStatus) -> Unit)
    override fun async(callback: (result: T?, status: PNStatus) -> Unit) {
        Executors.newSingleThreadExecutor()
            .execute { doAsync(callback) }
    }
}
