package com.pubnub.api.models.consumer.presence

import com.pubnub.api.JsonValue

/**
 * Result of the GetPresenceState operation.
 *
 * @property stateByUUID Map of UUIDs and the user states.
 */
class PNGetStateResult(
    val stateByUUID: Map<String, JsonValue>,
)
