package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.PNPublishResult

/**
 * @see [PubNub.publish]
 */
interface Publish : Endpoint<PNPublishResult> {
    val message: Any
    val channel: String
    val meta: Any?
    val shouldStore: Boolean?
    val usePost: Boolean
    val replicate: Boolean
    val ttl: Int?
}
