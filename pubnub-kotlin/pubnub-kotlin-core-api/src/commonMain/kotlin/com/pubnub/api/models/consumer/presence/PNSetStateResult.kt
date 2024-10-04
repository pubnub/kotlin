package com.pubnub.api.models.consumer.presence

import com.pubnub.api.JsonElement

/**
 * Result of the [PubNubImpl.setPresenceState] operation.
 *
 * @property state The actual state object.
 */
class PNSetStateResult(
    val state: JsonElement,
)
