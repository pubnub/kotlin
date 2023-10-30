package com.pubnub.internal.endpoints.pubsub

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.PNPublishResult

interface ISignal : ExtendedRemoteAction<PNPublishResult> {
    val channel: String
    val message: Any
}