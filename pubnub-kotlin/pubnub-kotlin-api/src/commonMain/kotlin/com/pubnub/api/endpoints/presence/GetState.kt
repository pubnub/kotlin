package com.pubnub.api.endpoints.presence

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.presence.PNGetStateResult

/**
 * @see [PubNub.getPresenceState]
 */
expect interface GetState : PNFuture<PNGetStateResult> {
}