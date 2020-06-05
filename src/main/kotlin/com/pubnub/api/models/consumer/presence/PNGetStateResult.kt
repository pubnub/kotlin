package com.pubnub.api.models.consumer.presence

import com.google.gson.JsonElement

class PNGetStateResult(
    val stateByUUID: Map<String, JsonElement>
)