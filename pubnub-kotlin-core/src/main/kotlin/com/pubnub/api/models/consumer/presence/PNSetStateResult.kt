package com.pubnub.api.models.consumer.presence

import com.google.gson.JsonElement

/**
 * Result of the [PubNubImpl.setPresenceState] operation.
 *
 * @property state The actual state object.
 */
class PNSetStateResult internal constructor(
    val state: JsonElement
)
