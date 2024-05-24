package com.pubnub.api.endpoints

import PubNub
import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.PNTimeResult

/**
 * @see [PubNub.time]
 */
actual interface Time : Endpoint<PNTimeResult>