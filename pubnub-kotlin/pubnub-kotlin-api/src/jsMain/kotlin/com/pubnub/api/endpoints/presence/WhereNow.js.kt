package com.pubnub.api.endpoints.presence

import PubNub
import com.pubnub.kmp.PNFuture
import com.pubnub.api.models.consumer.presence.PNWhereNowResult

/**
 * @see [PubNub.whereNow]
 */
actual interface WhereNow : PNFuture<PNWhereNowResult>

