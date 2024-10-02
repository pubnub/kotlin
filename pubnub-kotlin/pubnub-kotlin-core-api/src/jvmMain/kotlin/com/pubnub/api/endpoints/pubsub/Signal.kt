package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.PNPublishResult

/**
 * @see [PubNub.signal]
 */
actual interface Signal : Endpoint<PNPublishResult> {
    val channel: String
    val message: Any
}
