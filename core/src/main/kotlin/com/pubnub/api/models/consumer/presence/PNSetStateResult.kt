package com.pubnub.api.models.consumer.presence

import com.google.gson.JsonElement
import com.pubnub.api.PubNub

/**
 * Result of the [PubNub.setPresenceState] operation.
 *
 * @property state The actual state object.
 */
class PNSetStateResult internal constructor(
    val state: JsonElement
)
