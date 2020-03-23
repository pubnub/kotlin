package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement

data class PNPresenceEventResult(
    internal val event: String? = null,
    internal val uuid: String? = null,
    internal val timestamp: Long? = null,
    internal val occupancy: Int? = null,
    internal val state: JsonElement? = null,
    internal val channel: String? = null,
    internal val subscription: String? = null,
    internal val timetoken: Long? = null,
    internal val join: List<String>? = null,
    internal val leave: List<String>? = null,
    internal val timeout: List<String>? = null,
    internal val hereNowRefresh: Boolean? = null,
    internal val userMetadata: Any? = null
)