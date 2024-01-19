package com.pubnub.api.models.consumer.presence

import com.google.gson.JsonElement
import com.pubnub.internal.PubNub

/**
 * Result of the [PubNub.getPresenceState] operation.
 *
 * @property stateByUUID Map of UUIDs and the user states.
 */
class PNGetStateResult internal constructor(
    val stateByUUID: Map<String, JsonElement>
)
