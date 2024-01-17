package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.enums.PNOperationType
import com.pubnub.internal.Endpoint

open class MappingRemoteAction<T, U>(private val remoteAction: ExtendedRemoteAction<T>, private val function: (T) -> U) :
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

    override fun async(callback: PNCallback<U>) {
        remoteAction.async { r, s -> callback.onResponse(r?.let(function), s) }
    }
}

class MappingEndpoint<T, U>(private val endpoint: Endpoint<*, T>, private val function: (T) -> U) :
    MappingRemoteAction<T, U>(endpoint, function), com.pubnub.api.Endpoint<U> {
    override val queryParam: MutableMap<String, String>
        get() = endpoint.queryParam
}

class IdentityMappingEndpoint<T>(private val endpoint: Endpoint<*, T>) :
    MappingRemoteAction<T, T>(endpoint, { t -> t }), com.pubnub.api.Endpoint<T> {
    override val queryParam: MutableMap<String, String>
        get() = endpoint.queryParam
}

fun <T, U> ExtendedRemoteAction<T>.map(function: (T) -> U): ExtendedRemoteAction<U> {
    return MappingRemoteAction(this, function)
}

fun <T, U> Endpoint<*, T>.map(function: (T) -> U): com.pubnub.api.Endpoint<U> {
    return MappingEndpoint(this, function)
}

fun <T> Endpoint<*, T>.mapIdentity(): com.pubnub.api.Endpoint<T> {
    return IdentityMappingEndpoint(this)
}
