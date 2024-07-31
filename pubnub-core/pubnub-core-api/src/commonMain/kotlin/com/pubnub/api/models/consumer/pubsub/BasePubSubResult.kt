package com.pubnub.api.models.consumer.pubsub

import com.pubnub.api.JsonElement

interface PubSubResult : PNEvent {
    override val channel: String
    override val subscription: String?
    override val timetoken: Long
    val userMetadata: JsonElement?
    val publisher: String?
}

/**
 * @property channel The channel a PubNub API operation is related to.
 * @property subscription The subscriptions a PubNub API operation is related to.
 * @property timetoken Timetoken of the PubNub API operation in question.
 * @property userMetadata User metadata if any.
 * @property publisher The publisher of the PubNub API operation in question.
 */
data class BasePubSubResult(
    override val channel: String,
    override val subscription: String?,
    override val timetoken: Long,
    override val userMetadata: JsonElement?,
    override val publisher: String?,
) : PubSubResult
