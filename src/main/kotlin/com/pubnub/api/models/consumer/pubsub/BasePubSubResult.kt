package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement

open class BasePubSubResult(
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

