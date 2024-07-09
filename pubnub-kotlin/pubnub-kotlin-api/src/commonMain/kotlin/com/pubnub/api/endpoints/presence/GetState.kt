package com.pubnub.api.endpoints.presence

import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.getPresenceState]
 */
expect interface GetState : PNFuture<PNGetStateResult>
