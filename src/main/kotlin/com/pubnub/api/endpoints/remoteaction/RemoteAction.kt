package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PNCallback
import com.pubnub.api.PubNubException
import com.pubnub.core.CoreRemoteAction

interface RemoteAction<Output> : CoreRemoteAction<Output, PNCallback<Output>, PubNubException> {
    fun operationType(): com.pubnub.core.OperationType
}

typealias Cancelable = com.pubnub.core.Cancelable
