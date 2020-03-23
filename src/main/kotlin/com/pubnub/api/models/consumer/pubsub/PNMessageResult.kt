package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement

data class PNMessageResult(
    val basePubSubResult: BasePubSubResult,
    val messageResult: JsonElement
) : MessageResult(basePubSubResult, messageResult)