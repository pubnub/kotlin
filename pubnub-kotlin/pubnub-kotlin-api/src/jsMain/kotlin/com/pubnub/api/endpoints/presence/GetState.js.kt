package com.pubnub.api.endpoints.presence

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.presence.PNGetStateResult

/**
 * @see [PubNub.getPresenceState]
 */
actual interface GetState : Endpoint<PNGetStateResult> {
}