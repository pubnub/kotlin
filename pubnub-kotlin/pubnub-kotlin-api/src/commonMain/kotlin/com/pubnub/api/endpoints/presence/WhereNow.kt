package com.pubnub.api.endpoints.presence

import com.pubnub.api.models.consumer.presence.PNWhereNowResult
import com.pubnub.kmp.PNFuture

/**
 * @see [PubNub.whereNow]
 */
expect interface WhereNow : PNFuture<PNWhereNowResult>
