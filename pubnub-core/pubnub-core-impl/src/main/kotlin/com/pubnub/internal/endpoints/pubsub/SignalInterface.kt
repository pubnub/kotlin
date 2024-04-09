package com.pubnub.internal.endpoints.pubsub

import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.internal.EndpointInterface

interface SignalInterface : EndpointInterface<PNPublishResult> {
    val channel: String
    val message: Any
}
