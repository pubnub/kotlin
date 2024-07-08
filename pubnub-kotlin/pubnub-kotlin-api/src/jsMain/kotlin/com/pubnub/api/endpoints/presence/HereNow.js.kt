package com.pubnub.api.endpoints.presence

import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.hereNow]
 */
actual interface HereNow : PNFuture<PNHereNowResult>
