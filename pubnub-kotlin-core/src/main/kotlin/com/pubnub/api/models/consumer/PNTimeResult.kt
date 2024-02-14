package com.pubnub.api.models.consumer

import com.pubnub.internal.PubNubImpl

/**
 * Result of the [PubNubImpl.time] operation.
 *
 * @property timetoken Current time token.
 */
class PNTimeResult internal constructor(
    val timetoken: Long
)
