package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.core.CoreRemoteAction

interface ExtendedRemoteAction<Output> : RemoteAction<Output> {
    fun operationType(): PNOperationType
    fun retry()
}

interface RemoteAction<Output> : Cancelable, CoreRemoteAction<Output, PNStatus> {
    @Throws(PubNubException::class)
    fun sync(): Output?
}

interface Cancelable {
    fun silentCancel()
}
