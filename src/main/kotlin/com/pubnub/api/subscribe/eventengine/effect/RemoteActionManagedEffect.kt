package com.pubnub.api.subscribe.eventengine.effect

import com.pubnub.api.endpoints.remoteaction.RemoteAction
import com.pubnub.api.eventengine.ManagedEffect
import com.pubnub.api.models.consumer.PNStatus

class RemoteActionManagedEffect<T>(
    private val remoteAction: RemoteAction<T>,
    private val callback: (result: T?, status: PNStatus) -> Unit
) : ManagedEffect {
    override fun runEffect(completionBlock: () -> Unit) {
        remoteAction.async { result, status ->
            try {
                callback(result, status)
            } finally {
                completionBlock()
            }
        }
    }

    override fun cancel() {
        remoteAction.silentCancel()
    }
}

fun <T> RemoteAction<T>.toManagedEffect(callback: (result: T?, status: PNStatus) -> Unit): ManagedEffect {
    return RemoteActionManagedEffect(this, callback)
}
