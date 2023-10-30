package com.pubnub.api

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.callbacks.Result

abstract class Endpoint<OUTPUT> : ExtendedRemoteAction<OUTPUT>

abstract class DelegatingEndpoint<OUTPUT, DELEGATED>(remoteAction: ExtendedRemoteAction<DELEGATED>) :
    Endpoint<OUTPUT>() {

    private val remoteAction: ExtendedRemoteAction<OUTPUT> = convertAction(remoteAction)

    protected abstract fun convertAction(remoteAction: ExtendedRemoteAction<DELEGATED>): ExtendedRemoteAction<OUTPUT>

    @Throws(PubNubException::class)
    override fun sync(): OUTPUT {
        return remoteAction.sync()
    }

    override fun async(callback: (result: Result<OUTPUT>) -> Unit) {
        remoteAction.async { result -> callback(result) }
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
