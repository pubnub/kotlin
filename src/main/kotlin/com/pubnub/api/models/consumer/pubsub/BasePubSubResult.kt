package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement

open class BasePubSubResult(
    internal val channel: String,
    internal val subscription: String?,
    internal val timetoken: Long?,
    internal val userMetadata: JsonElement?,
    internal val publisher: String?

) {
    constructor(basePubSubResult: BasePubSubResult) : this(
        basePubSubResult.channel,
        basePubSubResult.subscription,
        basePubSubResult.timetoken,
        basePubSubResult.userMetadata,
        basePubSubResult.publisher
    )
}