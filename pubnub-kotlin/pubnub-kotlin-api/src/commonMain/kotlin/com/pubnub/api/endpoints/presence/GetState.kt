package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.presence.PNGetStateResult

/**
 * @see [PubNub.getPresenceState]
 */
expect interface GetState : Endpoint<PNGetStateResult> {
}