package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus

internal interface ExtendedRemoteAction<Output> : RemoteAction<Output> {
    fun operationType(): PNOperationType
    fun retry()
}

interface RemoteAction<Output> {
    @Throws(PubNubException::class)
    fun sync(): Output?
    fun async(callback: (result: Output?, status: PNStatus) -> Unit)
    fun silentCancel()
}
