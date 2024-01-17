package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.enums.PNOperationType

interface ExtendedRemoteAction<Output> : RemoteAction<Output> {
    fun operationType(): PNOperationType
    override fun retry()
}

interface RemoteAction<Output> : Cancelable {
    @Throws(PubNubException::class)
    fun sync(): Output?
    fun async(callback: PNCallback<Output>)
    fun retry()
    override fun silentCancel()
}

interface Cancelable {
    fun silentCancel()
}
