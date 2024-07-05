package com.pubnub.kmp.endpoints.presence

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.presence.PNSetStateResult

/**
 * @see [PubNub.setPresenceState]
 */
interface SetState : PNFuture<PNSetStateResult> {
}