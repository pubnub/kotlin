package com.pubnub.api.models.consumer

import com.pubnub.api.PubNub

/**
 * Result of the [PubNub.time] operation.
 *
 * @property timetoken Current time token.
 */
class PNTimeResult internal constructor(
    val timetoken: Long
)
