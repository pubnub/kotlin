package com.pubnub.api.models.consumer.history

import com.google.gson.JsonElement

class PNFetchMessagesResult(
    val channels: HashMap<String, List<PNFetchMessageItem>>
)

class PNFetchMessageItem(
    val message: JsonElement,
    val meta: JsonElement?,
    val timetoken: Long
) {
    var actions: Map<String, HashMap<String, List<Action>>>? = null
        internal set
}

class Action(
    val uuid: String,
    val actionTimetoken: String
)
