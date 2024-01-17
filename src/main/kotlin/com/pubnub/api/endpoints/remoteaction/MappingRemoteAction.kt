package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus

class MappingRemoteAction<T, U>(private val remoteAction: ExtendedRemoteAction<T>, private val function: (T) -> U) :
    ExtendedRemoteAction<U> {
    override fun operationType(): PNOperationType {
        return remoteAction.operationType()
    }

    override fun retry() {
        remoteAction.retry()
    }

    override fun sync(): U? = remoteAction.sync()?.let { function(it) }

    override fun silentCancel() {
        remoteAction.silentCancel()
    }

    override fun async(callback: (result: U?, status: PNStatus) -> Unit) {
        remoteAction.async { r, s ->
            callback(r?.let(function), s)
        }
    }
}

fun <T, U> ExtendedRemoteAction<T>.map(function: (T) -> U): ExtendedRemoteAction<U> {
    return MappingRemoteAction(this, function)
}
