package com.pubnub.kmp.endpoints.presence

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.presence.PNWhereNowResult

/**
 * @see [PubNub.whereNow]
 */
interface WhereNow : PNFuture<PNWhereNowResult> {
}