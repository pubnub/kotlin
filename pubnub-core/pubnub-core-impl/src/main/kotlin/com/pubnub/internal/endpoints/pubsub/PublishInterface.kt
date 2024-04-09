package com.pubnub.internal.endpoints.pubsub

import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.internal.EndpointInterface

interface PublishInterface : EndpointInterface<PNPublishResult> {
    val message: Any
    val channel: String
    val meta: Any?
    val shouldStore: Boolean?
    val usePost: Boolean
    val replicate: Boolean
    val ttl: Int?
}
