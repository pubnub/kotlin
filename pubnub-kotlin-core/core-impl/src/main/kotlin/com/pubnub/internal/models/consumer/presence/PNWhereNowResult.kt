package com.pubnub.internal.models.consumer.presence

import com.pubnub.internal.PubNubImpl

/**
 * Result of the [PubNubImpl.whereNow] operation.
 *
 * @property channels List of channels where a UUID is present.
 */
class PNWhereNowResult internal constructor(
    val channels: List<String>
)
