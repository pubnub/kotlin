package com.pubnub.api.endpoints.presence

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.presence.PNHereNowResult

/**
 * @see [PubNub.hereNow]
 */
actual interface HereNow : Endpoint<PNHereNowResult>

