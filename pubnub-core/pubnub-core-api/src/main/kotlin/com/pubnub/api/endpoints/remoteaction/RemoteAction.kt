package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.callbacks.Result
import java.util.function.Consumer

interface ExtendedRemoteAction<Output> : RemoteAction<Output> {
    fun operationType(): PNOperationType

    override fun retry()
}

interface RemoteAction<Output> : Cancelable {
    @Throws(PubNubException::class)
    fun sync(): Output

    fun async(callback: Consumer<Result<Output>>)

    fun retry()

    override fun silentCancel()
}

interface Cancelable {
    fun silentCancel()
}
