package com.pubnub.api.models.consumer.presence

import com.pubnub.internal.InternalPubNubClient

/**
 * Result of the [InternalPubNubClient.whereNow] operation.
 *
 * @property channels List of channels where a UUID is present.
 */
class PNWhereNowResult internal constructor(
    val channels: List<String>
) {
    companion object {
        fun from(data: com.pubnub.internal.models.consumer.presence.PNWhereNowResult): PNWhereNowResult {
            return PNWhereNowResult(data.channels)
        }
    }
}
