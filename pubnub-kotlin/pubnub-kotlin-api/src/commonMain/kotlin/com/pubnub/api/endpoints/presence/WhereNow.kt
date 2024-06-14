package com.pubnub.api.endpoints.presence

import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.presence.PNWhereNowResult

/**
 * @see [PubNub.whereNow]
 */
expect interface WhereNow : PNFuture<PNWhereNowResult> {
}