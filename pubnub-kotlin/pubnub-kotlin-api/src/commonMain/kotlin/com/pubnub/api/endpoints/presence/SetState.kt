package com.pubnub.api.endpoints.presence

import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.setPresenceState]
 */
expect interface SetState : PNFuture<PNSetStateResult>
