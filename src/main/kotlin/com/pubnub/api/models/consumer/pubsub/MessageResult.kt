package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement

open class MessageResult(
    basePubSubResult: BasePubSubResult,
    val message: JsonElement
) : BasePubSubResult(basePubSubResult)