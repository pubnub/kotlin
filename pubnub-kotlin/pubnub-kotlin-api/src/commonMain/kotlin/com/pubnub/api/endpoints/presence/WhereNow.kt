package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.presence.PNWhereNowResult

/**
 * @see [PubNub.whereNow]
 */
expect interface WhereNow : Endpoint<PNWhereNowResult> {
}