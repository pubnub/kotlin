package com.pubnub.core

class MappingRemoteAction<T, U, S : Status>(
    private val remoteAction: CoreRemoteAction<T, S>,
    private val function: (T) -> U
) : CoreRemoteAction<U, S> {

    override fun silentCancel() {
        remoteAction.silentCancel()
    }

    override fun async(callback: (result: U?, status: S) -> Unit) {
        remoteAction.async { r, s -> callback(r?.let(function), s) }
    }

    companion object {
        @JvmStatic
        fun <T, U, S : Status> CoreRemoteAction<T, S>.map(function: (T) -> U): CoreRemoteAction<U, S> {
            return MappingRemoteAction(this, function)
        }
    }
}


