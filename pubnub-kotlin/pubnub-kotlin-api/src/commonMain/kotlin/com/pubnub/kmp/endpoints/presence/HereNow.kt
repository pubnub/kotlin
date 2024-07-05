package com.pubnub.kmp.endpoints.presence

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.presence.PNHereNowResult

/**
 * @see [PubNub.hereNow]
 */
interface HereNow : PNFuture<PNHereNowResult> {
}