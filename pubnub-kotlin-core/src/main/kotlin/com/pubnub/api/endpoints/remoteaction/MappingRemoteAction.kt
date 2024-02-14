package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.api.v2.callbacks.onFailure
import com.pubnub.api.v2.callbacks.onSuccess
import java.util.function.Consumer

class MappingRemoteAction<T, U>(private val remoteAction: ExtendedRemoteAction<T>, private val function: (T) -> U) :
    ExtendedRemoteAction<U> {
    override fun operationType(): PNOperationType {
        return remoteAction.operationType()
    }

    override fun retry() {
        remoteAction.retry()
    }

    override fun sync(): U = function(remoteAction.sync())

    override fun silentCancel() {
        remoteAction.silentCancel()
    }

    override fun async(callback: Consumer<Result<U>>) {
        remoteAction.async { r ->

            r.onSuccess {
                try {
                    val newValue = function(it)
                    callback.accept(Result.success(newValue))
                } catch (e: Throwable) {
                    callback.accept(Result.failure(PubNubException.from(e)))
                }
            }.onFailure {
                callback.accept(Result.failure(it))
            }
        }
    }
}

fun <T, U> ExtendedRemoteAction<T>.map(function: (T) -> U): ExtendedRemoteAction<U> {
    return MappingRemoteAction(this, function)
}
