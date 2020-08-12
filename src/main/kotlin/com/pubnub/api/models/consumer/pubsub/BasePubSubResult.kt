package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement

/**
 * @property channel The channel a PubNub API operation is related to.
 * @property subscription The subscription a PubNub API operation is related to.
 * @property timetoken Timetoken of the PubNub API operation in question.
 * @property userMetadata User metadata if any.
 * @property publisher The publisher of the PubNub API operation in question.
 */
open class BasePubSubResult internal constructor(
    val channel: String,
    val subscription: String?,
    val timetoken: Long?,
    val userMetadata: JsonElement?,
    val publisher: String?

) {
    internal constructor(basePubSubResult: BasePubSubResult) : this(
        basePubSubResult.channel,
        basePubSubResult.subscription,
        basePubSubResult.timetoken,
        basePubSubResult.userMetadata,
        basePubSubResult.publisher
    )
}
