package com.pubnub.kmp.endpoints.presence

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.presence.PNGetStateResult

/**
 * @see [PubNub.getPresenceState]
 */
interface GetState : PNFuture<PNGetStateResult> {
}