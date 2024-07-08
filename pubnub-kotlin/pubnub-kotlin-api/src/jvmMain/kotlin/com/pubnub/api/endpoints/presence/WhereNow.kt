package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.presence.PNWhereNowResult

/**
 * @see [PubNub.whereNow]
 */
actual interface WhereNow : Endpoint<PNWhereNowResult> {
    val uuid: String
}
