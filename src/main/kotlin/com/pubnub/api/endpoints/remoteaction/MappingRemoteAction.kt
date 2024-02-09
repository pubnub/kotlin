package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.enums.PNOperationType

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

    override fun async(callback: (result: Result<U>) -> Unit) {
        remoteAction.async { r ->

            r.onSuccess {
                try {
                    val newValue = function(it)
                    callback(Result.success(newValue))
                } catch (e: Exception) {
                    callback(Result.failure(e))
                }
            }.onFailure {
                callback(Result.failure(it))
            }

        }
    }
}

fun <T, U> ExtendedRemoteAction<T>.map(function: (T) -> U): ExtendedRemoteAction<U> {
    return MappingRemoteAction(this, function)
}
