package com.pubnub.api.models.consumer.presence

import com.google.gson.JsonElement

data class PNSetStateResult(
    val state: JsonElement?
)