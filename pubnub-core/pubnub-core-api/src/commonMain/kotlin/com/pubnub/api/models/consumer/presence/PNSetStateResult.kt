package com.pubnub.api.models.consumer.presence

import com.pubnub.api.JsonValue

/**
 * Result of the [PubNubImpl.setPresenceState] operation.
 *
 * @property state The actual state object.
 */
class PNSetStateResult(
    val state: JsonValue,
)
