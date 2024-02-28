package com.pubnub.api.models.consumer.presence

import com.pubnub.internal.CorePubNubClient

/**
 * Result of the [CorePubNubClient.whereNow] operation.
 *
 * @property channels List of channels where a UUID is present.
 */
class PNWhereNowResult internal constructor(
    val channels: List<String>,
)
