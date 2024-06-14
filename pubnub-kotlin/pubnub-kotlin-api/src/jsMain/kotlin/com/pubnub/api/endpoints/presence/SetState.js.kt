package com.pubnub.api.endpoints.presence

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.presence.PNSetStateResult

/**
 * @see [PubNub.setPresenceState]
 */
actual interface SetState : PNFuture<PNSetStateResult>