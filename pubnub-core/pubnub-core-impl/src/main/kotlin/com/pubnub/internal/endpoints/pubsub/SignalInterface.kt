package com.pubnub.internal.endpoints.pubsub

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.PNPublishResult

interface SignalInterface : ExtendedRemoteAction<PNPublishResult> {
    val channel: String
    val message: Any
}
