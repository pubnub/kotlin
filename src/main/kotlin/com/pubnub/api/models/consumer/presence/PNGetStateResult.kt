package com.pubnub.api.models.consumer.presence

import com.google.gson.JsonElement

data class PNGetStateResult(
    val stateByUUID: Map<String, JsonElement>
)