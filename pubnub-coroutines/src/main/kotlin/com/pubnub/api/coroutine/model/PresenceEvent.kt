package com.pubnub.api.coroutine.model

import com.google.gson.JsonElement
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult

data class PresenceEvent(
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

internal fun PNPresenceEventResult.toPresenceEvent(): PresenceEvent {
    return PresenceEvent(
        event = event,
        uuid = uuid,
        timestamp = timestamp,
        occupancy = occupancy,
        state = state,
        channel = channel,
        subscription = subscription,
        timetoken = timetoken,
        join = join,
        leave = leave,
        timeout = timeout,
        hereNowRefresh = hereNowRefresh,
        userMetadata = userMetadata
    )
}
