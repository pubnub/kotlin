package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.PNPublishResult

/**
 * @see [PubNub.signal]
 */
interface Signal : com.pubnub.kmp.endpoints.pubsub.Signal, Endpoint<PNPublishResult> {
    val channel: String
    val message: Any
}
