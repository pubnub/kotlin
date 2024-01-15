package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus

interface ExtendedRemoteAction<Output> : RemoteAction<Output> {
    fun operationType(): PNOperationType
    override fun retry()
}

interface RemoteAction<Output> : Cancelable {
    @Throws(PubNubException::class)
    fun sync(): Output?
    fun async(callback: (result: Output?, status: PNStatus) -> Unit)
    fun retry()
    override fun silentCancel()
}

interface Cancelable {
    fun silentCancel()
}
