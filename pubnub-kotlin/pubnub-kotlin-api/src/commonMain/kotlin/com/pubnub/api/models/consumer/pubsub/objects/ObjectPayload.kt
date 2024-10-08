package com.pubnub.api.models.consumer.pubsub.objects

import com.pubnub.api.JsonElement

data class ObjectPayload(
    val source: String,
    val version: String,
    val event: String,
    val type: String,
    val data: JsonElement,
)
