package com.pubnub.api.models.server.history

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.pubnub.api.models.consumer.history.Action

data class ServerFetchMessageItem(
    val uuid: String?,
    val message: JsonElement,
    val meta: JsonElement?,
    val timetoken: Long,
    val actions: Map<String, Map<String, List<Action>>>? = null,
    @SerializedName("message_type")
    val messageType: Int?
)
