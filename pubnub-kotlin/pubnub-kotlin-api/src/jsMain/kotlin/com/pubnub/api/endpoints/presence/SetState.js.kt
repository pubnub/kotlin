package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.presence.PNSetStateResult

/**
 * @see [PubNub.setPresenceState]
 */
actual interface SetState : Endpoint<PNSetStateResult> {
}