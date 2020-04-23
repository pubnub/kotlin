package com.pubnub.api.models.consumer.history

import com.google.gson.JsonElement

class PNHistoryItemResult(
    val entry: JsonElement,
    val timetoken: Long? = null,
    val meta: JsonElement? = null
)