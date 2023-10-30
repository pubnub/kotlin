package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.callbacks.Result

interface ExtendedRemoteAction<Output> : RemoteAction<Output> {
    fun operationType(): PNOperationType
    override fun retry()
}

interface RemoteAction<Output> : Cancelable {
    @Throws(PubNubException::class)
    fun sync(): Output
    fun async(callback: (result: Result<Output>) -> Unit)
    fun retry()
    override fun silentCancel()
}

interface Cancelable {
    fun silentCancel()
}
