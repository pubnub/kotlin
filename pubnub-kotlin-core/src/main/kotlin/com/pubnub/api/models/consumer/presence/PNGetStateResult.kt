package com.pubnub.api.models.consumer.presence

import com.google.gson.JsonElement
import com.pubnub.internal.BasePubNub.PubNubImpl

/**
 * Result of the [PubNubImpl.getPresenceState] operation.
 *
 * @property stateByUUID Map of UUIDs and the user states.
 */
class PNGetStateResult internal constructor(
    val stateByUUID: Map<String, JsonElement>
)
