package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.models.consumer.PNTimeResult

/**
 * @see [PubNub.time]
 */
interface Time : com.pubnub.kmp.endpoints.Time, Endpoint<PNTimeResult>
