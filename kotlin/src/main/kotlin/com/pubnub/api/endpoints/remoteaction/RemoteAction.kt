package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus

interface ExtendedRemoteAction<Output> : RemoteAction<Output> {
    fun operationType(): PNOperationType
    fun retry()
}

interface RemoteAction<Output> : Cancelable, com.pubnub.core.RemoteAction<Output, PNStatus> {
    @Throws(PubNubException::class)
    fun sync(): Output?
}

interface Cancelable {
    fun silentCancel()
}
