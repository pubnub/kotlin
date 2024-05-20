package com.pubnub.api.models.consumer.pubsub.objects

import com.pubnub.api.JsonValue

data class ObjectPayload(
    val source: String,
    val version: String,
    val event: String,
    val type: String,
    val data: JsonValue,
)
