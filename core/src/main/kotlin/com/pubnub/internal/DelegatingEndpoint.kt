package com.pubnub.internal

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNubException
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType

abstract class DelegatingEndpoint<OUTPUT>() : DelegatingRemoteAction<OUTPUT>(), Endpoint<OUTPUT> {

    override val remoteAction: Endpoint<OUTPUT> by lazy(LazyThreadSafetyMode.NONE) { createAction() }

    abstract override fun createAction(): Endpoint<OUTPUT>

    override val queryParam: MutableMap<String, String>
        get() = remoteAction.queryParam
}

abstract class DelegatingRemoteAction<OUTPUT>() : ExtendedRemoteAction<OUTPUT> {

    protected open val remoteAction: ExtendedRemoteAction<OUTPUT> by lazy(LazyThreadSafetyMode.NONE) { createAction() }

    protected abstract fun createAction(): ExtendedRemoteAction<OUTPUT>

    @Throws(PubNubException::class)
    override fun sync(): OUTPUT? {
        return remoteAction.sync()
    }

    override fun async(callback: PNCallback<OUTPUT>) {
        remoteAction.async { result, status -> callback.onResponse(result, status) }
    }

    override fun retry() {
        remoteAction.retry()
    }

    override fun silentCancel() {
        remoteAction.silentCancel()
    }

    override fun operationType(): PNOperationType {
        return remoteAction.operationType()
    }
}