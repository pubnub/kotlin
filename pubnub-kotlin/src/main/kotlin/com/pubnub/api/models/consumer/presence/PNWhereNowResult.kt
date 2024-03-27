package com.pubnub.api.models.consumer.presence

import com.pubnub.internal.PubNubCore

/**
 * Result of the [PubNubCore.whereNow] operation.
 *
 * @property channels List of channels where a UUID is present.
 */
class PNWhereNowResult internal constructor(
    val channels: List<String>,
)
