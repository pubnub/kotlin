package com.pubnub.api.endpoints.presence

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.presence.PNHereNowResult

/**
 * @see [PubNub.hereNow]
 */
expect interface HereNow : PNFuture<PNHereNowResult> {
}