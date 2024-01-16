package com.pubnub.api.models.consumer.presence

/**
 * Result of the [com.pubnub.internal.PubNub.whereNow] operation.
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