package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement

class PNPresenceEventResult(
    val event: String? = null,
    val uuid: String? = null,
    val timestamp: Long? = null,
    val occupancy: Int? = null,
    val state: JsonElement? = null,
    val channel: String? = null,
    val subscription: String? = null,
    val timetoken: Long? = null,
    val join: List<String>? = null,
    val leave: List<String>? = null,
    val timeout: List<String>? = null,
    val hereNowRefresh: Boolean? = null,
    val userMetadata: Any? = null
)
