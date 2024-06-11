package com.pubnub.api.endpoints.presence

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.presence.PNSetStateResult

/**
 * @see [PubNub.setPresenceState]
 */
expect interface SetState : PNFuture<PNSetStateResult> {
}