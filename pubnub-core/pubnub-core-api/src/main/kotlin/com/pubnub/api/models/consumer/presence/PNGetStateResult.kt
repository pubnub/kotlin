package com.pubnub.api.models.consumer.presence

import com.google.gson.JsonElement

/**
 * Result of the GetPresenceState operation.
 *
 * @property stateByUUID Map of UUIDs and the user states.
 */
class PNGetStateResult(
    val stateByUUID: Map<String, JsonElement>,
)
