package com.pubnub.internal

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus

abstract class DelegatingEndpoint<OUTPUT, DELEGATED> internal constructor(remoteAction: ExtendedRemoteAction<DELEGATED>) :
    Endpoint<OUTPUT>() {

    private val remoteAction: ExtendedRemoteAction<OUTPUT> = convertAction(remoteAction)

    protected abstract fun convertAction(remoteAction: ExtendedRemoteAction<DELEGATED>): ExtendedRemoteAction<OUTPUT>

    @Throws(PubNubException::class)
    override fun sync(): OUTPUT? {
        return remoteAction.sync()
    }

    override fun async(callback: (result: OUTPUT?, status: PNStatus) -> Unit) {
        remoteAction.async { result, status -> callback(result, status) }
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