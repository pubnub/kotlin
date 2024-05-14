package com.pubnub.api.endpoints.presence

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.presence.PNHereNowResult

/**
 * @see [PubNub.hereNow]
 */
expect interface HereNow : Endpoint<PNHereNowResult> {
}